package com.cs.download.server.api.host;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "host-service")
public interface HostService extends PluginService {
  public boolean canHandle(HandleRequest request);

  public LoginResult login(LoginRequest request);

  public CrawlResult crawl(CrawlRequest request);

  public ExtractionResult extractLinks(ExtractionRequest request);
}
