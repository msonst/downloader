package com.cs.download.plugin;

import com.cs.download.api.plugin.PluginBase;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.plugin.impl.TorCtlServiceImpl;
import com.cs.download.plugin.impl.TorServiceImpl;

public class TorCtlPluginProvider extends PluginBase {

  static {
    registerService(new ServiceInfo(LifecycleState.STOPPED, "TorCtl", TorCtlServiceImpl.class));
    registerService(new ServiceInfo(LifecycleState.STOPPED, "Tor", TorServiceImpl.class));
  }
}
