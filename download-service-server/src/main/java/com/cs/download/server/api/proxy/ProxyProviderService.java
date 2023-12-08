package com.cs.download.server.api.proxy;

import com.cs.download.server.api.host.PluginService;

public interface ProxyProviderService extends PluginService {

  public ProxyResult next(ProxyRequest request);
}
