package com.cs.download.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);

  @Autowired
  private DiscoveryClient mDiscoveryClient;

  
  private ProxyManager() {
    
  }
  
  public ProxyResolver getProxy() {
   
    return new ProxyResolver(mDiscoveryClient);
  }

}
