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
package com.cs.download.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the file downloader.
 *
 * This class is annotated with {@code @Component}, indicating that it is a Spring bean
 * that can be automatically discovered and registered in the Spring application context.
 * It is used to configure the behavior of the {@code DownloadController} and {@code DownloadManager}.
 *
 * Constructs a new {@code DownloaderConfiguration} record.
 *
 */
@Configuration
@ConfigurationProperties(prefix = "download")
public class DownloaderConfiguration {

  private String mProxyType;
  private String mProxyHost;
  private int mProxyPort;
  private String mOutPath;
  private int mParallelDownloads;
  private int mThreadsPerDownload;
  private String mPersistFile;
  
  public String getProxyType() {
    return mProxyType;
  }
  public void setProxyType(String proxyType) {
    mProxyType = proxyType;
  }
  public String getProxyHost() {
    return mProxyHost;
  }
  public void setProxyHost(String proxyHost) {
    mProxyHost = proxyHost;
  }
  public int getProxyPort() {
    return mProxyPort;
  }
  public void setProxyPort(int proxyPort) {
    mProxyPort = proxyPort;
  }
  public String getOutPath() {
    return mOutPath;
  }
  public void setOutPath(String outPath) {
    mOutPath = outPath;
  }
  public int getParallelDownloads() {
    return mParallelDownloads;
  }
  public void setParallelDownloads(int parallelDownloads) {
    mParallelDownloads = parallelDownloads;
  }
  public int getThreadsPerDownload() {
    return mThreadsPerDownload;
  }
  public String getPersistFile() {
    return mPersistFile;
  }
  public void setThreadsPerDownload(int threadsPerDownload) {
    mThreadsPerDownload = threadsPerDownload;
  }
  public void setPersistFile(String persistFile) {
    mPersistFile = persistFile;
  }


}
