package com.cs.download.controller;

import java.net.Proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyManager {

  private String mProvider;
  

  public Proxy getProxy() {
    return null;
//    ProxyProviderService service = mServiceRegistry.getService(mProxyConfig.getProvider());
//    
//    ProxyRequest request = new ProxyRequest();
//    request.addProxyRequirement(new BandwidthProxyRequirement(10));
//    ProxyResult result = service.next(request);
//        
//    return result.getProxies().get(0);
  }

}
