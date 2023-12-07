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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.cs.download.api.plugin.PluginInfo;
import com.cs.download.api.plugin.service.PluginService;
import com.cs.download.api.plugin.service.ServiceInfo;
import com.cs.download.api.plugin.service.host.LifecycleResult;

/**
 * A registry for managing and starting services provided by plugins.
 */
@Service
public class PluginServiceRegistry implements ApplicationContextAware{

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginServiceRegistry.class);

  private ExecutorService mThreadPool;
  private final Map<String, Wrapper> mServices = new HashMap<>();

  private PluginDiscovery mPluginDiscovery;
  private @Autowired AutowireCapableBeanFactory mBeanFactory;

  
  private static ApplicationContext applicationContext;

  
  /**
   * Inner class representing a wrapper for a service provided by a plugin.
   */
  private class Wrapper {

    private ServiceInfo mServiceInfo;
    private List<Future<LifecycleResult>> mServiceInstances = new ArrayList<Future<LifecycleResult>>();

    /**
     * Constructs a Wrapper for a service.
     *
     * @param serviceFactory The factory for creating the service.
     * @param serviceInfo The information about the service.
     */
    public Wrapper(ServiceInfo serviceInfo) {
      mServiceInfo = serviceInfo;
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
    public void addServiceInstance(Future<LifecycleResult> futureServiceResult) {
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
          if (null != pluginInfo) {
            for (ServiceInfo serviceInfo : pluginInfo.getServices()) {
              String name = serviceInfo.getName();

              if (!mServices.containsKey(name)) {
                LOGGER.debug("NEW service " + name);
                mServices.put(name, new Wrapper(serviceInfo));
              }
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

    return mServices.values().stream().map(w -> w.getServiceInfo()).map(s -> s.getName()).collect(Collectors.toSet());
  }

  public Set<String> getServices(Class<? extends PluginService> clazz) {

    return mServices.values().stream().map(w -> w.getServiceInfo()).filter(s -> (clazz.isAssignableFrom(s.getPluginClass()))).map(s -> s.getName())
        .collect(Collectors.toSet());
  }

  private <T extends PluginService> T createInstance(Class<? extends PluginService> clazz) throws IllegalAccessException, InstantiationException {
    Class<T> type = null;

    // Get the actual type parameter used for this instance
    Type genericSuperclass = clazz.getGenericSuperclass();
    if (genericSuperclass instanceof ParameterizedType) {
      Type[] typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
      if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
        type = (Class<T>) typeArguments[0];
      }
    }
    return (null != type) ? type.newInstance() : null;
  }

  public <T extends PluginService> T getService(Class<? extends PluginService> clazz) {
    T ret = null;
    try {
      ret = (T) clazz.getDeclaredConstructor().newInstance();
    } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return ret;
  }

  /**
   * gets a service with the specified name.
   *
   * @param name The name of the service to start.
   */
  public <T extends PluginService> T getService(String name) {
    Wrapper wrapper = mServices.get(name);

    try {
      ServiceInfo serviceInfo = wrapper.getServiceInfo();
      T ret = getService(serviceInfo.getPluginClass());
      mBeanFactory.autowireBean(ret);
      wrapper.addServiceInstance(mThreadPool.submit(() -> ret.start()));
      return ret;
    } catch (ClassCastException e) {
      return null;
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext=applicationContext;
    
  }

}
