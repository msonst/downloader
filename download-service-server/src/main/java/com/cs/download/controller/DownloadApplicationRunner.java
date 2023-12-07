package com.cs.download.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DownloadApplicationRunner implements ApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadApplicationRunner.class);
  
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    LOGGER.info("Starting plugin monitoring");
  }
}