package com.cs.download.plugin;

import com.cs.download.api.plugin.PluginBase;
import com.cs.download.api.plugin.PluginState;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.plugin.impl.DefaultHostServiceImpl;

public class DefaultPluginProvider extends PluginBase {

  private PluginState mState = PluginState.PLUGGED;

  static {
    registerService(new ServiceInfo(LifecycleState.STOPPED, "Default", DefaultHostServiceImpl.class));
  }

}
