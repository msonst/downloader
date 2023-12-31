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
package com.cs.download.server.api;

public class RequestResult {

  private Long mDownloadId;
  private DownloadStatusCode mStatus;

  public RequestResult() {

  }

  public RequestResult(Long downloadId, DownloadStatusCode status) {
    setDownloadId(downloadId);
    setStatus(status);
  }

  public DownloadStatusCode getStatus() {
    return mStatus;
  }

  public void setStatus(DownloadStatusCode status) {
    mStatus = status;
  }

  public Long getDownloadId() {
    return mDownloadId;
  }

  public void setDownloadId(Long downloadId) {
    mDownloadId = downloadId;
  }

  @Override
  public String toString() {
    return "RequestResult [mDownloadId=" + mDownloadId + ", mStatus=" + mStatus + "]";
  }
}