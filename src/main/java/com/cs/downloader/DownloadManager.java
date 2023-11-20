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

public class DownloadManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

	private Proxy mProxy;
	private String mSavePath;
	private HashMap<URL, Download> mDownloads = new HashMap<URL, Download>();
	private HashMap<UUID, DownloadTask> mDownloadtasks = new HashMap<UUID, DownloadTask>();
	private HashMap<UUID, Future<DownloadStatusCode>> mExecuting = new HashMap<UUID, Future<DownloadStatusCode>>();
	private ExecutorService mPool;

	public DownloadManager(Proxy proxy, String savePath, int maxParallelDownloads) {
		mProxy = proxy;
		mSavePath = savePath;
		mPool = Executors.newFixedThreadPool(maxParallelDownloads);
	}

	public Download addDownload(URL url, String cookie, int threadCount,
			DownloadStatusListener downloadStatusListener) {

		Download ret = mDownloads.get(url);
		if (null == ret) {

			UUID id = UUID.randomUUID();

			DownloadTask downloadTask = new DownloadTask(url, cookie, mProxy, mSavePath, threadCount,
					downloadStatusListener);

			ret = new Download(id);

			mDownloads.put(url, ret);
			mDownloadtasks.put(id, downloadTask);

			LOGGER.debug("Download added {}", url);
		} else
			LOGGER.info("Download already existing {}", url);

		return ret;
	}

	public Download addDownload(URL url, String cookie) {
		return addDownload(url, cookie, 1, null);
	}

	public void start(Download download) {

		LOGGER.debug("Start {}", download);
		UUID id = download.getDownloadId();
		DownloadTask downloadTask = mDownloadtasks.get(id);
		mExecuting.put(id, mPool.submit(downloadTask));
	}

	public void startAll() {
		LOGGER.debug("Starting all {}", mDownloads.size());
		mDownloads.values().forEach(d -> start(d));
	}

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

	private List<Future<DownloadStatusCode>> getRunning() {
		return mExecuting.values().parallelStream().filter(t -> !t.isDone()).collect(Collectors.toList());
	}

	public boolean isComplete(Download download) {
		Future<DownloadStatusCode> future = mExecuting.get(download.getDownloadId());
		return (null != future) ? future.isDone() : true;
	}
}
