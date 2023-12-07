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
package com.cs.download.api.plugin;

import java.util.ArrayList;
import java.util.List;

import com.cs.download.api.plugin.service.ServiceInfo;

/*
 * Copyright [Year] [Author/Company]
 * [License Information]
 */

/**
 * Represents information about a plugin, including its status and associated service factories.
 */
public class PluginInfo {

  /**
   * The status of the plugin.
   */
  private final PluginState mStatus;

  /**
   * The list of service factories associated with the plugin.
   */
  private final List<ServiceInfo> mServices = new ArrayList<>();

  /**
   * Constructs a new PluginInfo instance with the specified status and list of service factories.
   *
   * @param status    The status of the plugin.
   * @param services The list of services associated with the plugin.
   */
  public PluginInfo(final PluginState status, final List<ServiceInfo> services) {
    mStatus = status;
    mServices.addAll(services);
  }

  /**
   * Gets the status of the plugin.
   *
   * @return The status of the plugin.
   */
  public final PluginState getStatus() {
    return mStatus;
  }

  /**
   * Gets the list of services associated with the plugin.
   *
   * @return The list of services.
   */
  public final List<ServiceInfo> getServices() {
    return new ArrayList<ServiceInfo>(mServices);
  }
}