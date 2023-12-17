package com.cs.download;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for starting the file downloader application.
 *
 * This class is annotated with {@code @SpringBootApplication}, indicating that it is the
 * entry point for the Spring Boot application and includes the configuration, component scanning,
 * and auto-configuration for the application.
 */
// watch for Consider defining a bean of type 'com.netflix.discovery.DiscoveryClient' in your configuration.
// this requires com.netflix.discovery.discoveryclient!!!!!!
@EntityScan(basePackages = "com.cs.download.entity")
@EnableJpaRepositories(basePackages = "com.cs.download.repository")
@SpringBootApplication(scanBasePackages = { "com.cs.download" }, exclude = {
    org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class })
public class TorApplication {
  /**
   * Main method to launch the Spring Boot application.
   *
   * @param args Command line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(TorApplication.class, args);
  }
}
