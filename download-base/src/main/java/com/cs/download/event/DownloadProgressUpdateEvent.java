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

import java.io.Serializable;

import com.cs.download.DownloadStatusCode;

/**
 * The {@code DownloadProgressUpdateEvent} class represents an event indicating the progress update
 * during the download process.
 *
 * @see DownloadEvent
 */
public class DownloadProgressUpdateEvent extends DownloadEvent implements Serializable {

  private static final long serialVersionUID = 2353566631945415574L;
  private DownloadStatusCode mStatus;

  /**
   * Constructs a new {@code DownloadProgressUpdateEvent} with the specified source.
   *
   * @param source The object on which the event initially occurred.
   */
  public DownloadProgressUpdateEvent(Object source, DownloadStatusCode status) {
    super(source);
    mStatus = status;
  }

  public DownloadStatusCode getStatus() {
    return mStatus;
  }

  public void setStatus(DownloadStatusCode status) {
    mStatus = status;
  }
}