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
package com.cs.download.event;

/**
 * An abstract adapter class for the {@link DownloadStatusListener} interface.
 * This class provides empty implementations of all methods for easy extension.
 */
public abstract class DownloadAdapter implements DownloadStatusListener {

  // Empty implementations for all methods in DownloadStatusListener

  /**
   * {@inheritDoc}
   */
  @Override
  public void onProgress(PartProgressUpdateEvent event) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onProgress(DownloadProgressUpdateEvent event) {

  }
}
