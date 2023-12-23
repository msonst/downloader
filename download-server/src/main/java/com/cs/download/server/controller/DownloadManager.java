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
package com.cs.download.server.controller;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

import com.cs.download.server.api.DownloadStatusCode;
import com.cs.download.server.entity.DownloadEntity;
import com.cs.download.server.event.DownloadProgressUpdateEvent;
import com.cs.download.server.event.DownloadStatusListener;
import com.cs.download.server.event.PartProgressUpdateEvent;
import com.cs.download.server.proxy.BandwidthProxyRequirement;
import com.cs.download.server.proxy.ProxyListConfig;
import com.cs.download.server.service.DownloadEntityService;

/**
 * The {@code DownloadManager} class manages multiple file download tasks
 * asynchronously using a thread pool. It allows adding, starting, and
 * monitoring the status of downloads.
 * <p>
 * The manager maintains a pool of worker threads to execute download tasks
 * concurrently.
 * </p>
 *
 * @see UUID
 * @see DownloadTask
 * @see java.util.concurrent.ExecutorService
 */
@Component
public class DownloadManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);
  private final String mSavePath;
  private HashMap<Long, Download> mDownloads = new HashMap<>();
  private final ExecutorService mPool;

  private @Autowired ProxyListConfig mProxyListConfig;
  private List<ProxyRessource> mProxies= new ArrayList<>();
  private @Autowired DownloadEntityService mDownloadEntityService;
  private DownloaderConfiguration mConfig;


  
  /**
   * Constructs a new {@code DownloadManager} with the specified parameters.
   *
   * @param proxy                The proxy to be used for downloads.
   * @param savePath             The path where downloaded files will be saved.
   * @param maxParallelDownloads The maximum number of parallel downloads allowed.
   */
  @ConstructorBinding
  public DownloadManager(DownloaderConfiguration config) {
    mConfig = config;
    mSavePath = mConfig.getOutPath();
    mPool = Executors.newFixedThreadPool(mConfig.getParallelDownloads());
  }

  /**
   * Adds a new download task to the manager with default parameters.
   *
   * @param url    The URL of the file to download.
   * @param cookie The cookie to use for authentication.
   * @return The {@link Long} id.
   * @throws URISyntaxException 
   */
  public Long addDownload(final String url, String cookie) throws URISyntaxException {
    Long ret = null;

    List<DownloadEntity> downloads = mDownloadEntityService.findByUrl(url);

    if (!downloads.isEmpty()) {
      ret = downloads.get(0).getId();
      LOGGER.info("Download already existing {}", ret);
      return ret;
    }

    String fileName = FilenameUtils.getName(url);
    if (fileName.isEmpty())
      fileName = url.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

    String filePath = Paths.get(mSavePath, fileName).toString();

    DownloadEntity downloadEntity = new DownloadEntity(url, cookie, filePath, DownloadStatusCode.INITIALIZED.getResponseCode());
    ret = mDownloadEntityService.saveEntity(downloadEntity);

    LOGGER.debug("Download added id={} url={}", ret, url);

    return ret;

  }

  public ProxyRessource getProxyRessource() {
    ProxyRessource ret = null;

    if (mProxies.isEmpty()) {
      mProxyListConfig.getProxies().forEach(c -> {
        String host = c.getHost();
        int privoxyPort = c.getPrivoxyPort();

        mProxies.add(new ProxyRessource(new Proxy(Type.HTTP, new InetSocketAddress(host, privoxyPort)),
            new BandwidthProxyRequirement(mProxyListConfig.getMinMBits())));
      });
    }

    List<ProxyRessource> freeChannels = mProxies.stream().filter(c -> c.isFree()).collect(Collectors.toList());
    if (!freeChannels.isEmpty())
      ret = freeChannels.get(0);

    return ret;
  }
  /**
   * Starts the specified download task.
   *
   * @param d The {@link UUID} object representing the download task to
   *                 start.
   * @return DownloadStatusCode
   * @throws MalformedURLException 
   */
  public DownloadStatusCode start(Long d) {
    LOGGER.debug("Start {}", d);

    DownloadEntity downloadEntity = mDownloadEntityService.getEntityById(d);

    //    Download download = mDownloads.get(d);

    if (null != downloadEntity) {
      try {
        ResumableDownload resumableDownload = new ResumableDownload(downloadEntity.getUrl(), downloadEntity.getFilePath(), downloadEntity.getCookie(),
            mConfig.getThreadsPerDownload(), getProxyRessource(), new DownloadStatusListener() {

              @Override
              public void onProgress(DownloadProgressUpdateEvent event) {
                mDownloadEntityService.saveEntity(null);
                //          LOGGER.debug("onProgress url={} event.status={}", url, event.getStatus());
              }

              @Override
              public void onProgress(PartProgressUpdateEvent event) {
                //          LOGGER.debug("onProgress url={} event.partid={}", url, event.getPartId());
              }
            });
        Download download = new Download(resumableDownload);
        mDownloads.put(d, download);
        download.setFuture(mPool.submit(download.getCallable()));
      } catch (MalformedURLException e) {
        return DownloadStatusCode.ERROR.setMessage(e.getMessage());
      }

    }
    return DownloadStatusCode.INITIALIZED;
  }

  /**
   * Starts all download tasks added to the manager.
   * @return DownloadStatusCode
   */
//  public DownloadStatusCode startAll() {
//    LOGGER.debug("Starting all");
//
//    mDownloadEntityService.getAllEntities().stream().map(d -> start(d.getId()));
//
//    return DownloadStatusCode.INITIALIZED;
//  }

  /**
   * Waits for all download tasks to complete.
   */
  public void waitForCompletion() {
    for (List<Long> running = getRunning(); !running.isEmpty(); running = getRunning()) {
      LOGGER.debug("Downloads not finished {}", running);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    LOGGER.debug("Downloads finished");
  }

  /**
   * Retrieves a list of running download tasks.
   *
   * @return A list of {@link java.util.UUID} objects representing running
   *         download tasks.
   */
  private List<Long> getRunning() {
    return mDownloads.entrySet().parallelStream().filter(e -> !e.getValue().isDone()).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()))
        .keySet().stream().collect(Collectors.toList());
  }

  /**
   * Checks whether the specified download task is complete.
   *
   * @param downloadId The {@link Integer} object representing the download task.
   * @return {@code true} if the download is complete, {@code false} otherwise.
   */
  public DownloadStatusCode getStatus(Long downloadId) {
    Future<DownloadStatusCode> future = mDownloads.get(downloadId).getFuture();
    try {
      return (null != future) ? (future.isDone()) ? future.get() : DownloadStatusCode.DOWNLOADING : DownloadStatusCode.COMPLETE;
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return DownloadStatusCode.ERROR.setMessage(e.getMessage());
    }
  }

  public File[] listFiles() {
    return new File(mSavePath).listFiles();
  }
}
