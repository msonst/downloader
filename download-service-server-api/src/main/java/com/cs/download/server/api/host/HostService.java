package com.cs.download.server.api.host;

public interface HostService extends DownloadService {
  public boolean canHandle(HandleRequest request);

  public LoginResult login(LoginRequest request);

  public CrawlResult crawl(CrawlRequest request);

  public ExtractionResult extractLinks(ExtractionRequest request);
}
