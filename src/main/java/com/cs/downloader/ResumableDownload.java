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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.downloader.event.DownloadStatusListener;
import com.cs.downloader.event.PartProgressUpdateEvent;
import com.cs.downloader.jmx.DownloadStats;

/**
 * A class representing a resumable file download.
 */
public class ResumableDownload {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResumableDownload.class);
	private DownloadStatusListener mListener;
	private int mNumThreads;
	private Proxy mProxy;
	private String mSimpleName;
	private DownloadStats[] mTaskStati;

	/**
	 * Constructs a new ResumableDownload instance.
	 *
	 * @param numThreads The number of threads to use for parallel downloading.
	 * @param proxy      The proxy to be used for the connection.
	 * @param listener   The listener to receive download status updates.
	 */
	public ResumableDownload(int numThreads, Proxy proxy, DownloadStatusListener listener) {
		mNumThreads = numThreads;
		mProxy = proxy;
		mListener = listener;
		mTaskStati = new DownloadStats[mNumThreads];
	}

	/**
	 * Initiates a synchronous download.
	 *
	 * @param url          The URL of the resource to be downloaded.
	 * @param saveFileName The name of the local file to save the resource to.
	 * @param cookie       The cookie for authentication.
	 * @return The status code of the download operation.
	 * @throws IOException          If an I/O error occurs during the download.
	 * @throws InterruptedException If the download is interrupted.
	 * @throws ExecutionException   If an error occurs during the execution of the
	 *                              download tasks.
	 */
	public DownloadStatusCode downloadSync(URL url, String saveFileName, String cookie)
			throws IOException, InterruptedException, ExecutionException {

		DownloadStatusCode responseCode = DownloadStatusCode.ERROR;
		long fileSize = -1;

		String[] split = saveFileName.split("/");
		mSimpleName = split[split.length - 1];

		LOGGER.info("Starting fileUrl={}, saveFileName={}, numThreads={}, cookie={}, proxy={}", url, saveFileName,
				mNumThreads, cookie, mProxy);

		for (int i = 0; (i < 5) && (fileSize < 0); i++) {

			HttpURLConnection connection = (HttpURLConnection) ((null != mProxy) ? url.openConnection(mProxy)
					: url.openConnection());
			if (null == connection)
				return DownloadStatusCode.ERROR;

			connection.setRequestProperty("Cookie", cookie);

			fileSize = connection.getContentLengthLong();
			if (fileSize == -1) {
				String contentRange = connection.getHeaderField("Content-Range");
				if (contentRange != null && contentRange.startsWith("bytes")) {
					fileSize = Long.parseLong(contentRange.split("/")[1]);
				}
			}

			responseCode = DownloadStatusCode.fromResponseCode(connection.getResponseCode());
			connection.disconnect();
		}

		if (fileSize < 0 || !responseCode.isOK()) {
			LOGGER.warn("Unable to get filesize fileUrl={} responseCode={}", url, responseCode);
			return responseCode;
		}

		LOGGER.debug("Size of download {}", fileSize);
		DownloadStatusCode statusCode = start(url, saveFileName, cookie, fileSize);

		return statusCode;
	}

	/**
	 * Initiates the parallel download tasks.
	 *
	 * @param url          The URL of the resource to be downloaded.
	 * @param saveFileName The name of the local file to save the resource to.
	 * @param cookie       The cookie for authentication.
	 * @param fileSize     The size of the resource to be downloaded.
	 * @return The status code of the download operation.
	 * @throws InterruptedException If the download is interrupted.
	 * @throws IOException          If an I/O error occurs during the download.
	 */
	private DownloadStatusCode start(URL url, String saveFileName, String cookie, long fileSize)
			throws InterruptedException, IOException {
		ExecutorService pool = Executors.newFixedThreadPool(mNumThreads);
		long chunkSize = fileSize / mNumThreads;

		List<Future<TaskResult>> running = new ArrayList<Future<TaskResult>>();

		for (int i = 0; i < mNumThreads; i++) {
			long startRange = i * chunkSize;
			long endRange = (i == mNumThreads - 1) ? fileSize - 1 : (i + 1) * chunkSize - 1;

			running.add(pool.submit(
					new PartDownloadTask(this, url, saveFileName + "." + i, startRange, endRange, i, cookie, mProxy)));
		}

		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.HOURS);
		LOGGER.debug("Tasks returned");

		List<TaskResult> finished = running.stream().map(p -> {
			try {
				return p.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).filter(p -> (null != p && p.isComplete())).collect(Collectors.toList());

		if (finished.size() == mNumThreads) {
			mergeFiles(saveFileName, mNumThreads);
			LOGGER.debug("Download complete url={}", url);
			return DownloadStatusCode.COMPLETE;
		} else {
			LOGGER.debug("Missing parts. Finished are {}", finished);
			return DownloadStatusCode.ERROR;
		}
	}

	/**
	 * Merges the downloaded chunks into the final file.
	 *
	 * @param fileName   The name of the final merged file.
	 * @param numThreads The number of download threads.
	 * @throws IOException If an I/O error occurs during the merge.
	 */
	private void mergeFiles(String fileName, int numThreads) throws IOException {

		LOGGER.debug("Merge started. fileName={}", fileName);

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			for (int i = 0; i < numThreads; i++) {
				try (FileInputStream inputStream = new FileInputStream(fileName + "." + i)) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
				}
				// Delete the partial files
				new File(fileName + "." + i).delete();
			}
		}
		LOGGER.debug("Merge done. fileName={}", fileName);
	}

	/**
	 * Updates the status of a download task.
	 *
	 * @param threadId      The identifier of the download thread.
	 * @param startRange    The start range of the download task.
	 * @param endRange      The end range of the download task.
	 * @param bytes         The number of bytes downloaded in this update.
	 * @param transferred   The total number of bytes transferred so far.
	 * @param fileSzAtStart The file size at the start of the download task.
	 * @param start         The start time of the download task.
	 * @param end           The end time of the download task.
	 */
	public synchronized void updateTaskStatus(int threadId, long startRange, long endRange, long bytes,
			long transferred, long fileSzAtStart, long start, long end) {

		mTaskStati[threadId].update(threadId, startRange, endRange, bytes, transferred, fileSzAtStart, start, end);

		CompletableFuture.runAsync(() -> mListener.onProgress(new PartProgressUpdateEvent(this, threadId, startRange,
				endRange, bytes, transferred, fileSzAtStart, start, end)));
	}
}
