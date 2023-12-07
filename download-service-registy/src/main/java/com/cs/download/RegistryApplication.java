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
package com.cs.download;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for starting the file downloader application.
 *
 * This class is annotated with {@code @SpringBootApplication}, indicating that it is the
 * entry point for the Spring Boot application and includes the configuration, component scanning,
 * and auto-configuration for the application.
 */

@EnableEurekaServer
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class RegistryApplication {
  /**
   * Main method to launch the Spring Boot application.
   *
   * @param args Command line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(RegistryApplication.class, args);
  }
}
