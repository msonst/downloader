package com.cs.download.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "plugin")
public class PluginConfig {

  private String mMonitoringPath;

  /**
   * 
   * @return
   */
  public String getMonitoringPath() {
    return mMonitoringPath;
  }

  /**
   * 
   * @param monitoringPath
   */
  public void setMonitoringPath(String monitoringPath) {
    mMonitoringPath = monitoringPath;
  }

}
