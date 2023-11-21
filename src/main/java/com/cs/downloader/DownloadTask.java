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

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.downloader.event.DownloadStatusListener;

/**
 * The {@code DownloadTask} class represents a task for downloading a file from a given URL using multiple threads.
 * It implements the {@link java.util.concurrent.Callable} interface for asynchronous execution.
 * <p>
 * This class is designed to facilitate efficient and concurrent file downloads with configurable parameters such as
 * thread count, proxy settings, and download status listeners.
 * </p>
 *
 * @see java.util.concurrent.Callable
 */
public class DownloadTask implements Callable<DownloadStatusCode> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);

    private URL mURL;
    private String mCookie;
    private DownloadStatusListener mDownloadStatusListener;
    private Proxy mProxy;
    private String mSavePath;
    private int mThreadCount;

    /**
     * Constructs a new {@code DownloadTask} with the specified parameters.
     *
     * @param url                      The URL of the file to download.
     * @param cookie                   The cookie to use for authentication.
     * @param proxy                    The proxy to be used for the download.
     * @param savePath                 The path where the downloaded file will be saved.
     * @param threadCount              The number of threads to use for downloading.
     * @param downloadStatusListener  The listener for download status updates.
     */
    public DownloadTask(URL url, String cookie, Proxy proxy, String savePath, int threadCount,
                        DownloadStatusListener downloadStatusListener) {
    	mURL = url;
		mCookie = cookie;
		mProxy = proxy;
		mSavePath = savePath;
		mThreadCount = threadCount;
		mDownloadStatusListener = downloadStatusListener;
    }

    /**
     * Executes the download task.
     *
     * @return The download status code indicating the result of the download.
     * @throws Exception If an error occurs during the download process.
     */
    @Override
    public DownloadStatusCode call() throws Exception {
        LOGGER.debug("Started {}", this);

        ResumableDownload download = new ResumableDownload(mThreadCount, mProxy, mDownloadStatusListener);

        try {
            return download.downloadSync(mURL, mSavePath, mCookie);
        } catch (IOException | InterruptedException | ExecutionException e) {
            LOGGER.error("Error during download", e);
            return DownloadStatusCode.ERROR;
        }
    }

    /**
     * Returns a string representation of the {@code DownloadTask}.
     *
     * @return A string representation of the {@code DownloadTask}.
     */
    @Override
    public String toString() {
        return "DownloadTask [mURL=" + mURL + ", mCookie=" + mCookie +
                ", mDownloadStatusListener=" + mDownloadStatusListener +
                ", mProxy=" + mProxy + ", mSavePath=" + mSavePath +
                ", mThreadCount=" + mThreadCount + "]";
    }

    /**
     * Gets the URL of the file to be downloaded.
     *
     * @return The URL of the file to be downloaded.
     */
    public URL getUrl() {
        return mURL;
    }
}

