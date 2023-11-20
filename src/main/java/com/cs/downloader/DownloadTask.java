package com.cs.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.downloader.event.DownloadProgressUpdateEvent;
import com.cs.downloader.event.DownloadStatusListener;
import com.cs.downloader.event.PartProgressUpdateEvent;

public class DownloadTask implements Callable<DownloadStatusCode> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);

	private URL mURL;
	private String mCookie;
	private DownloadStatusListener mDownloadStatusListener;
	private Proxy mProxy;
	private String mSavePath;
	private int mThreadCount;

	public DownloadTask(URL url, String cookie, Proxy proxy, String savePath, int threadCount,
			DownloadStatusListener downloadStatusListener) {
		mURL = url;
		mCookie = cookie;
		mProxy = proxy;
		mSavePath = savePath;
		mThreadCount = threadCount;
		mDownloadStatusListener = downloadStatusListener;
	}

	@Override
	public DownloadStatusCode call() {
		LOGGER.debug("Started {}", this);

		ResumableDownload download = new ResumableDownload(mThreadCount, mProxy, mDownloadStatusListener);

		try {
			return download.downloadSync(mURL, mSavePath, mCookie);
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return DownloadStatusCode.ERROR;
	}

	@Override
	public String toString() {
		return "Download [mMockURL=" + mURL + ", mCookie=" + mCookie + ", mDownloadStatusListener="
				+ mDownloadStatusListener + ", mProxy=" + mProxy + ", mSavePath=" + mSavePath + ", mThreadCount="
				+ mThreadCount + "]";
	}

	public URL getUrl() {
		return mURL;
	}
}
