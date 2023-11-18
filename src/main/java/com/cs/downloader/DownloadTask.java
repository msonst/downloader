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
package com.cs.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.downloader.jmx.DownloadStats;

class DownloadTask implements Callable<TaskResult> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);
	private static final long BLOCK_SZ = 10 * 1024 * 1024;

	private final URL mUrl;
	private final String mPartName;
	private long mStartRange;
	private final long mEndRange;
	private final int mId;
	private final String mCookie;
	private final Proxy mProxy;
	private long mFileSzAtStart;
	private ResumableDownload mDownloader;
	private DownloadStats mDownloadStats = new DownloadStats();
	private String mSimpleName;
	private long mAdjustedStartRange;

	public DownloadTask(ResumableDownload downloader, URL url, String saveFileName, long startRange, long endRange, int id, String cookie,
			Proxy proxy) {
		mDownloader = downloader;
		mUrl = url;
		mPartName = saveFileName;
		mAdjustedStartRange = mStartRange = startRange;
		mEndRange = endRange;
		mId = id;
		mCookie = cookie;
		mProxy = proxy;

		LOGGER.debug("New task {} ", toString());

		try {
			ObjectName objectName = new ObjectName("com.cs.downloader:name=DownloadStats-" + mSimpleName + "-" + id);
			mDownloadStats = new DownloadStats();
			ManagementFactory.getPlatformMBeanServer().registerMBean(mDownloadStats, objectName);
		} catch (Exception e) {
			e = null;
		}
	}

	@Override
	public String toString() {
		return "DownloadTask [threadId=" + mId + ", adjustedStartRange=" + mAdjustedStartRange + ", endRange=" + mEndRange + ", partName="
				+ mPartName + ", url=" + mUrl + ", cookie=" + mCookie + ", proxy=" + mProxy + "]";
	}

	@Override
	public TaskResult call() throws Exception {

		DownloadStatusCode responseCode = DownloadStatusCode.ERROR;
		InputStream inputStream = null;

		LOGGER.debug("Called {} (adjusted) start {} end {}", mId, mAdjustedStartRange, mEndRange);

		for (int retry = 0; retry < 5; retry++) {

			File file = new File(mPartName);
			if (file.exists()) {
				mFileSzAtStart = file.length();
				mAdjustedStartRange = mStartRange + mFileSzAtStart;
				LOGGER.debug("Adjusted start {} ", mAdjustedStartRange);
			}
			if (mAdjustedStartRange >= mEndRange) {
				LOGGER.debug("Download {} already complete", mId);
				return new TaskResult(mId, DownloadStatusCode.COMPLETE);
			}

			LOGGER.debug("Init connection {}", mId);

			try {
				HttpURLConnection connection = (HttpURLConnection) ((null != mProxy) ? mUrl.openConnection(mProxy) : mUrl.openConnection());
				connection.setRequestProperty("Range", "bytes=" + mAdjustedStartRange + "-" + mEndRange);
				connection.setRequestProperty("Cookie", mCookie);

				inputStream = connection.getInputStream();
				responseCode = DownloadStatusCode.fromResponseCode(connection.getResponseCode());
				LOGGER.debug("Init connection {} responseCode {}", mId, responseCode);
			} catch (IOException e) {
				LOGGER.error("Init connection failed {} [{}]", mId, e.getMessage());
			}

			if (!responseCode.isOK()) {
				return new TaskResult(mId, responseCode);
			}

			try {
				responseCode = download(inputStream);
				if (responseCode.isOK()) {
					return new TaskResult(mId, responseCode);
				} else {
					LOGGER.error("Download {} failed attempt {} responseCode={}", mId, retry, responseCode);
				}
			} catch (IOException e) {
				LOGGER.error("Download {} [{}]", mId, e.getMessage());
			}
		}
		return new TaskResult(mId, responseCode);
	}

	private DownloadStatusCode download(InputStream inputStream) throws IOException {

		LOGGER.debug("Download {}", mId);

		try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
				FileOutputStream fos = new FileOutputStream(mPartName, true);
				FileChannel fc = fos.getChannel();) {

			long transferred = 0, startPos = mAdjustedStartRange, bytes;
			long start = System.currentTimeMillis(), end = 0;

			long remaining = mEndRange - mAdjustedStartRange + 1;
			long count = (remaining > BLOCK_SZ) ? BLOCK_SZ : remaining;
			int zeroCounter = 0;

			while (count > 0) {
				LOGGER.debug("transfer id={} start={} count={}", mId, startPos, count);

				bytes = fc.transferFrom(readableByteChannel, startPos, count);

				zeroCounter = (bytes <= 0) ? zeroCounter + 1 : 0;

				if (zeroCounter >= 3 && (count > 0)) {
					LOGGER.warn("INCOMPLETE transfer id={} bytes={}", mId, bytes);
					return DownloadStatusCode.INCOMPLETE;
				}
				end = System.currentTimeMillis();
				transferred += bytes;

				mDownloader.updateTaskStatus(mId, startPos, mEndRange, bytes, transferred, mFileSzAtStart, start, end);

				start = System.currentTimeMillis();

				remaining = mEndRange - mAdjustedStartRange - transferred + 1;
				count = (remaining > BLOCK_SZ) ? BLOCK_SZ : remaining;
				startPos = transferred + mAdjustedStartRange;
			}

			LOGGER.debug("Download complete {}. Bytes read {}/{}", mId, transferred, mEndRange - mAdjustedStartRange + 1);

			return DownloadStatusCode.COMPLETE;
		}
	}
}