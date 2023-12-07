package com.cs.download.plugin.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "proxy.docker")
public class DockerConfig {

  private String mPath;
  private String mRegistryUrl;
  private String mHost;
  private String mWorkDir;

  public String getPath() {
    return mPath;
  }

  public String getRegistryUrl() {
    return mRegistryUrl;
  }

  public String getHost() {
    return mHost;
  }

  public String getWorkDir() {
    return mWorkDir;
  }

}
