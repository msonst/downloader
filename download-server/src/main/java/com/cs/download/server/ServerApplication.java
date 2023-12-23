/*
 *     Copyright 2023 Michael Sonst @ https://www.corporate-startup.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cs.download.server;

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

@EntityScan(basePackages = "com.cs.download.server.entity")
@EnableJpaRepositories(basePackages = "com.cs.download.server.repository")
@SpringBootApplication(scanBasePackages = { "com.cs.download" }, exclude = {
    org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class })
public class ServerApplication {
  /**
   * Main method to launch the Spring Boot application.
   *
   * @param args Command line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }
}

