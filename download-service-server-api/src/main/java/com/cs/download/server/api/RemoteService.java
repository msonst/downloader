package com.cs.download.server.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class RemoteService {
  @Autowired
  private DiscoveryClient discoveryClient;

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient myApiWebClient(WebClient.Builder webClientBuilder) {
    String myApiServiceUrl = discoverServiceUrl("my-api-service");
    return webClientBuilder.baseUrl(myApiServiceUrl).build();
  }

  private String discoverServiceUrl(String serviceName) {
    List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
    if (instances.isEmpty()) {
      throw new RuntimeException("No instances available for service: " + serviceName);
    }
    return instances.get(0).getUri().toString();
  }
}
