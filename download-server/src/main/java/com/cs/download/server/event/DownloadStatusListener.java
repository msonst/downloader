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
package com.cs.download.server.event;

/**
 * An interface for receiving notifications about the progress of a download.
 */
public interface DownloadStatusListener extends java.util.EventListener {

  /**
   * Called when a part of the download progresses.
   *
   * @param event The event containing information about the part progress.
   */
  public void onProgress(PartProgressUpdateEvent event);

  /**
   * Called when a the download progresses.
   *
   * @param event The event containing information about the part progress.
   */
  public void onProgress(DownloadProgressUpdateEvent event);
}
