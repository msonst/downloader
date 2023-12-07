package com.cs.download.plugin;

import com.cs.download.api.plugin.PluginBase;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.plugin.impl.GitHubHostServiceImpl;

public class GitHubPluginProvider extends PluginBase {

  static {
    registerService(new ServiceInfo(LifecycleState.STOPPED, "GitHub", GitHubHostServiceImpl.class));
  }
}
