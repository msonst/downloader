package com.cs.download.api.plugin.service.proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "proxy.ssh")
public class SshConfig {

  private String mUser;
  private String mPassword;
  private int mPort;
  
  public String getUser() {

    return mUser;
  }

  public String getPassword() {

    return mPassword;
  }


  public int getPort() {

    return mPort;
  }


  public void setUser(String user) {
    mUser = user;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public void setPort(int port) {
    mPort = port;
  }
}
