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

import com.cs.download.plugin.api.PluginInfo;
import com.cs.download.plugin.api.Service;
import com.cs.download.plugin.api.ServiceFactory;
import com.cs.download.plugin.api.ServiceInfo;
import com.cs.download.plugin.api.ServiceResult;

/**
 * A registry for managing and starting services provided by plugins.
 */
public class PluginServiceRegistry {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginServiceRegistry.class);

  private ExecutorService mThreadPool;
  private final Map<String, Wrapper> mServices = new HashMap<>();
  private List<PluginDiscovery> mPluginDiscoveries = new ArrayList<PluginDiscovery>();

  /**
   * Inner class representing a wrapper for a service provided by a plugin.
   */
  private class Wrapper {

    private ServiceFactory mServiceFactory;
    private ServiceInfo mServiceInfo;
    private List<Future<ServiceResult>> mServiceInstances = new ArrayList<Future<ServiceResult>>();

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
    public void addServiceInstance(Future<ServiceResult> futureServiceResult) {
      mServiceInstances.add(futureServiceResult);
    }
  }

  /**
   * Constructs a ServiceRegistry with a cached thread pool.
   */
  public PluginServiceRegistry() {
    mThreadPool = Executors.newCachedThreadPool();
  }

  /**
   * Adds a plugin path for discovering services.
   *
   * @param path The path to the plugin.
   * @return The ServiceRegistry instance for method chaining.
   */
  public PluginServiceRegistry addPluginPath(String path) {
    PluginDiscovery pluginDiscovery = new PluginDiscovery(path);
    mPluginDiscoveries.add(pluginDiscovery);

    pluginDiscovery.addListener(new PluginAdapter() {

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

    return this;
  }

  /**
   * Starts the plugin discoveries and returns whether they all started successfully.
   *
   * @return True if all plugin discoveries started successfully, false otherwise.
   */
  public boolean start() {
    return mPluginDiscoveries.parallelStream().map(d -> d.start()).allMatch(r -> r);
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

    Service service = wrapper.getServiceFactory().getService(wrapper.getServiceInfo());

    wrapper.addServiceInstance(mThreadPool.submit(() -> service.start()));

    LOGGER.debug("started");
  }
}
