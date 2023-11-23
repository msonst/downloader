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

/**
 * Represents the result of a service operation, including the state and an optional message.
 */
public class ServiceResult {

  /**
   * Constructs a new ServiceResult instance with the specified state.
   *
   * @param state The state of the service result.
   */
  public ServiceResult(ServiceState state) {
    // Implementation details
  }

  /**
   * Constructs a new ServiceResult instance with the specified state and message.
   *
   * @param state The state of the service result.
   * @param msg   The additional message associated with the result.
   */
  public ServiceResult(ServiceState state, String msg) {
    // Implementation details
  }
}
