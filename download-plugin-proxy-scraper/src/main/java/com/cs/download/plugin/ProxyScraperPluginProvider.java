package com.cs.download.plugin;

import com.cs.download.api.plugin.PluginBase;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.plugin.impl.ProxyScraperServiceImpl;

public class ProxyScraperPluginProvider extends PluginBase {

  static {
    registerService(new ServiceInfo(LifecycleState.STOPPED, "FreeProxyLists", ProxyScraperServiceImpl.class));
  }
}
