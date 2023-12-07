package com.cs.download.api.plugin.service.host;

import com.cs.download.api.plugin.service.PluginService;

public interface HostService extends PluginService {
  public boolean canHandle(HandleRequest request);

  public LoginResult login(LoginRequest request);

  public CrawlResult crawl(CrawlRequest request);

  public ExtractionResult extractLinks(ExtractionRequest request);
}
