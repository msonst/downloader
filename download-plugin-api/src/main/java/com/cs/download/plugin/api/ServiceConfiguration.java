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
 */package com.cs.download.plugin.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the configuration for a service, allowing key-value pairs to be stored and retrieved.
 */
public class ServiceConfiguration {

  /**
   * The map to store key-value pairs representing the configuration.
   */
  private Map<String, Object> mValues = new HashMap<>();

  /**
   * Puts a key-value pair into the configuration.
   *
   * @param key   The key for the configuration entry.
   * @param value The value associated with the key.
   */
  public void put(String key, Object value) {
    mValues.put(key, value);
  }

  /**
   * Retrieves the value associated with a given key.
   *
   * @param key The key for which to retrieve the value.
   * @return The value associated with the key, or null if the key is not present.
   */
  public Object get(String key) {
    return mValues.get(key);
  }

  /**
   * Updates the current configuration with values from another configuration.
   *
   * @param configuration The configuration containing the updated values.
   */
  public void update(ServiceConfiguration configuration) {
    mValues.keySet().forEach(k -> put(k, configuration.get(k)));
  }

  /**
   * Converts the configuration to a map.
   *
   * @return The configuration as a map of key-value pairs.
   */
  //    public Map<?, ?> asMap() {
  //        return mValues.cl;
  //    }

  /**
   * Returns a string representation of the service configuration.
   *
   * @return A string representation of the service configuration.
   */
  @Override
  public String toString() {
    return "ServiceConfiguration [mValues=" + mValues + "]";
  }
}
