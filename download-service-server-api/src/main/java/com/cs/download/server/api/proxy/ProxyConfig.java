package com.cs.download.server.api.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "proxy")
public class ProxyConfig {

  private String mProvider;
  @Autowired  
  private TorConfig mTor;
  @Autowired  
  private SshConfig mSsh;

  public String getProvider() {
    return mProvider;
  }

  public void setProvider(String provider) {
    mProvider = provider;
  }

  public TorConfig getTor() {
    return mTor;
  }

  public void setTor(TorConfig tor) {
    mTor = tor;
  }

  public SshConfig getSsh() {
    return mSsh;
  }

  public void setSsh(SshConfig ssh) {
    mSsh = ssh;
  }

}
