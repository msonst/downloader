package com.cs.download.server.proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tor")
public class ProxyConfig {

  private String mHost;
  private int mPrivoxyPort;

  public String getHost() {
    return mHost;
  }

  public void setHost(String host) {
    mHost = host;
  }

  public int getPrivoxyPort() {
    return mPrivoxyPort;
  }

  public void setPrivoxyPort(int privoxyPort) {
    mPrivoxyPort = privoxyPort;
  }
}
