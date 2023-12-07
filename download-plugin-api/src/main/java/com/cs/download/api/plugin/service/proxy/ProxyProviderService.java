package com.cs.download.api.plugin.service.proxy;

import com.cs.download.api.plugin.service.PluginService;

public interface ProxyProviderService extends PluginService {

  public ProxyResult next(ProxyRequest request);
}
