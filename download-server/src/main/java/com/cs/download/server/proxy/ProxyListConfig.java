package com.cs.download.server.proxy;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "proxylist")
public class ProxyListConfig {

  private int mMinMBits;
  private List<ProxyConfig> mProxies;

  public List<ProxyConfig> getProxies() {
    return mProxies;
  }

  public void setProxies(List<ProxyConfig> proxies) {
    mProxies = proxies;
  }

  public int getMinMBits() {
    return mMinMBits;
  }

  public void setMinMBits(int minMBits) {
    mMinMBits = minMBits;
  }
  
  
}
