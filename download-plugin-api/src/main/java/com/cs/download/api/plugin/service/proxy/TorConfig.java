package com.cs.download.api.plugin.service.proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "proxy.tor")
public class TorConfig {

  private String mHost;
  private int mPort;
  private String mPassword;
  private int mCtlPort;

  public String getHost() {

    return mHost;
  }

  public int getPort() {

    return mPort;
  }

  public String getPassword() {

    return mPassword;
  }

  public int getCtlPort() {
    return mCtlPort;
  }

  public void setHost(String host) {
    mHost = host;
  }

  public void setPort(int port) {
    mPort = port;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public void setCtlPort(int ctlPort) {
    mCtlPort = ctlPort;
  }
}
