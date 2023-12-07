package com.cs.download.api.plugin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.spi.Plugin;

public class PluginBase implements Plugin {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginBase.class);

  private final static List<ServiceInfo> SERVICES = new ArrayList<ServiceInfo>();
  private PluginInfo mPluginInfo = null;

  public PluginBase() {
    mPluginInfo = new PluginInfo(PluginState.PLUGGED, SERVICES);
  }

  @Override
  public PluginInfo getPluginInfo() {
    return mPluginInfo;
  }

  @Override
  public List<ServiceInfo> getServiceInfo() {
    return SERVICES;
  }

  protected static void registerService(ServiceInfo serviceInfo) {
    SERVICES.add(serviceInfo);
  }
}
