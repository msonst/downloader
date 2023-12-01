package com.cs.download.plugin.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.api.plugin.lifecyccle.PluginLifecycle;
import com.cs.download.api.plugin.lifecyccle.PluginLifecycleState;
import com.cs.download.api.plugin.service.ServiceFactory;
import com.cs.download.api.plugin.service.ServiceInfo;

public class DefaultServiceFactoryImpl implements ServiceFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServiceFactoryImpl.class);

  private static final List<ServiceInfo> SERVICES = new ArrayList<ServiceInfo>();

  static {
    SERVICES.add(new ServiceInfo(PluginLifecycleState.STOPPED, DefaulltHostServiceImpl.class.getName()));
  }

  @Override
  public PluginLifecycle getService(ServiceInfo serviceInfo) {

    return null;
  }

  @Override
  public List<ServiceInfo> getServiceInfo() {
    return SERVICES;
  }

}
