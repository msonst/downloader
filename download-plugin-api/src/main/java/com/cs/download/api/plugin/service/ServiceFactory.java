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
 */package com.cs.download.api.plugin.service;

import java.util.List;

import com.cs.download.api.plugin.lifecyccle.PluginLifecycle;

/**
 * Interface representing a factory for creating services and retrieving information about available services.
 */
public interface ServiceFactory {

  /**
   * Creates a new instance of a service based on the provided service information.
   *
   * @param serviceInfo The information about the service to be created.
   * @return A new instance of the service.
   */
  PluginLifecycle getService(ServiceInfo serviceInfo);

  /**
   * Retrieves a list of service information, describing the available services that can be created by this factory.
   *
   * @return A list of service information.
   */
  List<ServiceInfo> getServiceInfo();
}
