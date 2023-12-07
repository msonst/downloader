package com.cs.download;

import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.cs.download.api.plugin.service.proxy.BandwidthProxyRequirement;
import com.cs.download.api.plugin.service.proxy.ProxyConfig;
import com.cs.download.api.plugin.service.proxy.ProxyProviderService;
import com.cs.download.api.plugin.service.proxy.ProxyRequest;
import com.cs.download.api.plugin.service.proxy.ProxyResult;
import com.cs.download.plugin.PluginServiceRegistry;

@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyManager {

  private String mProvider;
  
  private @Autowired PluginServiceRegistry mServiceRegistry;
  private @Autowired ProxyConfig mProxyConfig;

  public Proxy getProxy() {
    ProxyProviderService service = mServiceRegistry.getService(mProxyConfig.getProvider());
    
    ProxyRequest request = new ProxyRequest();
    request.addProxyRequirement(new BandwidthProxyRequirement(10));
    ProxyResult result = service.next(request);
        
    return result.getProxies().get(0);
  }

}
