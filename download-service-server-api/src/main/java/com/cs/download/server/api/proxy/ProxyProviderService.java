package com.cs.download.server.api.proxy;

import com.cs.download.server.api.host.DownloadService;

public interface ProxyProviderService extends DownloadService {

  public ProxyResult next(ProxyRequest request);
}
