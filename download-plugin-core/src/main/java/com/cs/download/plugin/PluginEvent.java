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

import java.util.EventObject;

import com.cs.download.api.plugin.PluginInfo;

/**
 * Event class representing changes in the state of plugins.
 */
public class PluginEvent extends EventObject {

  private static final long serialVersionUID = 1812962915814650478L;

  /**
   * Constructs a new PluginEvent with the specified array of plugin information.
   *
   * @param info An array of PluginInfo objects representing the changed plugins.
   */
  public PluginEvent(PluginInfo[] info) {
    super(info);
  }

  /**
   * Constructs a new PluginEvent with the specified PluginInfo object.
   *
   * @param info The PluginInfo object representing the changed plugin.
   */
  public PluginEvent(PluginInfo info) {
    this(new PluginInfo[] { info });
  }

  /**
   * Gets an array of PluginInfo objects representing the changed plugins.
   *
   * @return An array of PluginInfo objects.
   */
  public PluginInfo[] getPluginInfo() {
    return (PluginInfo[]) getSource();
  }
}