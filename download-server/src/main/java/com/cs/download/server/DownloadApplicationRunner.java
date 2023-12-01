package com.cs.download.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cs.download.plugin.PluginServiceRegistry;

@Component
public class DownloadApplicationRunner implements ApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadApplicationRunner.class);
  
  @Autowired
  private PluginServiceRegistry mServiceRegistry;
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    LOGGER.info("Starting plugin monitoring");
    
    mServiceRegistry.startMonitoring();
  }
}