package com.cs.download.api.plugin.service.host;

import java.net.URI;

import com.cs.download.api.plugin.lifecyccle.PluginLifecycle;

public interface HostService extends PluginService, PluginLifecycle {
  public boolean canHandle(HandleRequest request);

  public LoginResult login(LoginRequest request);

  public CrawlResult crawl(CrawlRequest request);

  public ExtractionResult extractLinks(ExtractionRequest request);
}
