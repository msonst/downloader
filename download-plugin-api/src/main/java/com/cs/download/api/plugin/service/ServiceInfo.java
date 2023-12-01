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

import com.cs.download.api.plugin.lifecyccle.PluginLifecycleState;

/**
 * Represents information about a service, including its name and state.
 */
public class ServiceInfo {

  /**
   * The name of the service.
   */
  private String mName;

  /**
   * The state of the service.
   */
  private PluginLifecycleState mState;

  /**
   * Constructs a new ServiceInfo instance with the specified state and name.
   *
   * @param state The state of the service.
   * @param name  The name of the service.
   */
  public ServiceInfo(PluginLifecycleState state, String name) {
    mState = state;
    mName = name;
  }

  /**
   * Gets the name of the service.
   *
   * @return The name of the service.
   */
  public String getName() {
    return mName;
  }

  /**
   * Gets the state of the service.
   *
   * @return The state of the service.
   */
  public PluginLifecycleState getState() {
    return mState;
  }
}
