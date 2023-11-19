/*
 * Copyright 2023 Michael Sonst @ https://www.corporate-startup.com Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.cs.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the result of a download task.
 */
class TaskResult {
  private static final Logger LOGGER = LoggerFactory.getLogger(TaskResult.class);

  private int mThreadId;
  private DownloadStatusCode mResponseCode;

  /**
   * Constructs a new TaskResult instance.
   *
   * @param threadId The identifier of the download thread.
   * @param responseCode The status code of the download task.
   */
  public TaskResult(int threadId, DownloadStatusCode responseCode) {
    mThreadId = threadId;
    mResponseCode = responseCode;

    LOGGER.debug("New result {}", toString());
  }

  /**
   * Gets the identifier of the download thread.
   *
   * @return The identifier of the download thread.
   */
  public int getThreadId() {
    return mThreadId;
  }

  /**
   * Gets the status code of the download task.
   *
   * @return The status code of the download task.
   */
  public DownloadStatusCode getResponseCode() {
    return mResponseCode;
  }

  /**
   * Indicates whether the download task is complete.
   *
   * @return True if the download task is complete; otherwise, false.
   */
  public boolean isComplete() {
    return mResponseCode.isOK();
  }

  /**
   * Returns a string representation of the TaskResult.
   *
   * @return A string representation of the TaskResult.
   */
  @Override
  public String toString() {
    return "TaskResult [mThreadId=" + mThreadId + ", mResponseCode=" + mResponseCode + "]";
  }
}
