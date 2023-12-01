/*
 *     Copyright 2023 Michael Sonst @ https://www.corporate-startup.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cs.download.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Service;

import com.cs.download.api.plugin.lifecyccle.PluginLifecycle;
import com.cs.download.api.plugin.lifecyccle.PluginLifecycleResult;
import com.cs.download.api.plugin.service.ServiceFactory;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.spi.PluginInfo;

/**
 * A registry for managing and starting services provided by plugins.
 */
@Service
public class PluginServiceRegistry {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginServiceRegistry.class);

  private ExecutorService mThreadPool;
  private final Map<String, Wrapper> mServices = new HashMap<>();

  private PluginDiscovery mPluginDiscovery;

  /**
   * Inner class representing a wrapper for a service provided by a plugin.
   */
  private class Wrapper {

    private ServiceFactory mServiceFactory;
    private ServiceInfo mServiceInfo;
    private List<Future<PluginLifecycleResult>> mServiceInstances = new ArrayList<Future<PluginLifecycleResult>>();

    /**
     * Constructs a Wrapper for a service.
     *
     * @param serviceFactory The factory for creating the service.
     * @param serviceInfo The information about the service.
     */
    public Wrapper(ServiceFactory serviceFactory, ServiceInfo serviceInfo) {
      mServiceFactory = serviceFactory;
      mServiceInfo = serviceInfo;
    }

    /**
     * Gets the service factory.
     *
     * @return The service factory.
     */
    public ServiceFactory getServiceFactory() {
      return mServiceFactory;
    }

    /**
     * Gets the service information.
     *
     * @return The service information.
     */
    public ServiceInfo getServiceInfo() {
      return mServiceInfo;
    }

    /**
     * Adds a service instance to the list.
     *
     * @param futureServiceResult The Future representing the service instance.
     */
    public void addServiceInstance(Future<PluginLifecycleResult> futureServiceResult) {
      mServiceInstances.add(futureServiceResult);
    }
  }

  /**
   * Constructs a ServiceRegistry with a cached thread pool.
   */
  @ConstructorBinding
  public PluginServiceRegistry(PluginDiscovery pluginDiscovery) {
    mPluginDiscovery = pluginDiscovery;
    mThreadPool = Executors.newCachedThreadPool();

    mPluginDiscovery.addListener(new PluginAdapter() {

      @Override
      public void onModified(PluginEvent event) {
      }

      @Override
      public void onDeleted(PluginEvent event) {
      }

      @Override
      public void onCreated(PluginEvent event) {
        LOGGER.debug("NEW plugin");
        for (PluginInfo pluginInfo : event.getPluginInfo()) {
          for (ServiceFactory serviceFactory : pluginInfo.getFactories()) {
            for (ServiceInfo serviceInfo : serviceFactory.getServiceInfo()) {
              String name = serviceInfo.getName();
              LOGGER.debug("NEW service " + name);
              mServices.put(name, new Wrapper(serviceFactory, serviceInfo));
            }
          }
        }
      }
    });
  }

  public void startMonitoring() {
    mPluginDiscovery.start();
  }

  /**
   * Gets the names of the services in the registry.
   *
   * @return The set of service names.
   */
  public Set<String> getServices() {
    return new HashSet<>(mServices.keySet());
  }

  /**
   * Starts a service with the specified name.
   *
   * @param name The name of the service to start.
   * @throws InterruptedException If the thread is interrupted while waiting.
   * @throws ExecutionException If the computation threw an exception.
   */
  public void startService(String name) throws InterruptedException, ExecutionException {
    Wrapper wrapper = mServices.get(name);

    PluginLifecycle service = wrapper.getServiceFactory().getService(wrapper.getServiceInfo());

    wrapper.addServiceInstance(mThreadPool.submit(() -> service.start()));

    LOGGER.debug("started");
  }

}
