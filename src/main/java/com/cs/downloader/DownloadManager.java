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

import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.downloader.event.DownloadStatusListener;

/**
 * The {@code DownloadManager} class manages multiple file download tasks asynchronously using a thread pool.
 * It allows adding, starting, and monitoring the status of downloads.
 * <p>
 * The manager maintains a pool of worker threads to execute download tasks concurrently.
 * </p>
 *
 * @see Download
 * @see DownloadTask
 * @see java.util.concurrent.ExecutorService
 */
public class DownloadManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

    private Proxy mProxy;
    private String mSavePath;
    private HashMap<URL, Download> mDownloads = new HashMap<>();
    private HashMap<UUID, DownloadTask> mDownloadTasks = new HashMap<>();
    private HashMap<UUID, Future<DownloadStatusCode>> mExecuting = new HashMap<>();
    private ExecutorService mPool;

    /**
     * Constructs a new {@code DownloadManager} with the specified parameters.
     *
     * @param proxy               The proxy to be used for downloads.
     * @param savePath            The path where downloaded files will be saved.
     * @param maxParallelDownloads The maximum number of parallel downloads allowed.
     */
    public DownloadManager(Proxy proxy, String savePath, int maxParallelDownloads) {
        mProxy = proxy;
        mSavePath = savePath;
        mPool = Executors.newFixedThreadPool(maxParallelDownloads);
    }

    /**
     * Adds a new download task to the manager.
     *
     * @param url                    The URL of the file to download.
     * @param cookie                 The cookie to use for authentication.
     * @param threadCount            The number of threads to use for downloading.
     * @param downloadStatusListener The listener for download status updates.
     * @return The {@link Download} object representing the added download task.
     */
    public Download addDownload(URL url, String cookie, int threadCount,
                                DownloadStatusListener downloadStatusListener) {
        Download ret = mDownloads.get(url);
        if (null == ret) {
            UUID id = UUID.randomUUID();
            DownloadTask downloadTask = new DownloadTask(url, cookie, mProxy, mSavePath, threadCount,
                    downloadStatusListener);
            ret = new Download(id);
            mDownloads.put(url, ret);
            mDownloadTasks.put(id, downloadTask);
            LOGGER.debug("Download added {}", url);
        } else
            LOGGER.info("Download already existing {}", url);
        return ret;
    }

    /**
     * Adds a new download task to the manager with default parameters.
     *
     * @param url    The URL of the file to download.
     * @param cookie The cookie to use for authentication.
     * @return The {@link Download} object representing the added download task.
     */
    public Download addDownload(URL url, String cookie) {
        return addDownload(url, cookie, 1, null);
    }

    /**
     * Starts the specified download task.
     *
     * @param download The {@link Download} object representing the download task to start.
     */
    public void start(Download download) {
        LOGGER.debug("Start {}", download);
        UUID id = download.downloadId();
        DownloadTask downloadTask = mDownloadTasks.get(id);
        mExecuting.put(id, mPool.submit(downloadTask));
    }

    /**
     * Starts all download tasks added to the manager.
     */
    public void startAll() {
        LOGGER.debug("Starting all {}", mDownloads.size());
        mDownloads.values().forEach(d -> start(d));
    }

    /**
     * Waits for all download tasks to complete.
     */
    public void waitForCompletion() {
        for (List<Future<DownloadStatusCode>> running = getRunning(); !running.isEmpty(); running = getRunning()) {
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
     * @return A list of {@link java.util.concurrent.Future} objects representing running download tasks.
     */
    private List<Future<DownloadStatusCode>> getRunning() {
        return mExecuting.values().parallelStream().filter(t -> !t.isDone()).collect(Collectors.toList());
    }

    /**
     * Checks whether the specified download task is complete.
     *
     * @param download The {@link Download} object representing the download task.
     * @return {@code true} if the download is complete, {@code false} otherwise.
     */
    public boolean isComplete(Download download) {
        Future<DownloadStatusCode> future = mExecuting.get(download.downloadId());
        return (null != future) ? future.isDone() : true;
    }
}
