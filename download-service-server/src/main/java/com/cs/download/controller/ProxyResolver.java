package com.cs.download.controller;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.cs.download.server.api.proxy.BandwidthProxyRequirement;
import com.cs.download.server.api.proxy.ProxyRequest;
import com.cs.download.server.api.proxy.ProxyResult;

import reactor.core.publisher.Mono;

public class ProxyResolver {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyResolver.class);
  private DiscoveryClient mDiscoveryClient;
  private Proxy mProxy;

  public ProxyResolver(DiscoveryClient discoveryClient) {
    mDiscoveryClient = discoveryClient;
  }

  public Proxy get() {

    if (null == mProxy) {
      List<ServiceInstance> services = mDiscoveryClient.getInstances("dl-tor");
      LOGGER.debug("services" + services);

      ProxyRequest request = new ProxyRequest();
      request.addProxyRequirement(new BandwidthProxyRequirement(1));
      ProxyResult proxyResult = WebClient.builder()
          //.exchangeStrategies(strategies)
          .baseUrl("http://dinm5cg3150hg4:9092/").build().method(HttpMethod.GET).uri(uriBuilder -> uriBuilder.path("/tor/next").build())
          .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), ProxyRequest.class).retrieve().bodyToMono(ProxyResult.class).block();
      LOGGER.debug("Tor" + proxyResult);

      mProxy = new Proxy(Proxy.Type.valueOf(proxyResult.type()), new InetSocketAddress(proxyResult.host(), proxyResult.privoxyPort()));
    }
    return mProxy;
  }
}
