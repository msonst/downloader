package com.cs.download.controller;

import java.util.concurrent.Future;

import com.cs.download.server.api.DownloadStatusCode;

public class Download {

  private DownloadTask mDownloadTask;
  private Future<DownloadStatusCode> mFuture;

  public Download(DownloadTask downloadTask) {
    mDownloadTask = downloadTask;
  }

  public DownloadTask getTask() {
    return mDownloadTask;
  }

  public void setFuture(Future<DownloadStatusCode> future) {
    mFuture = future;
  }

  public Future<DownloadStatusCode> getFuture() {
    return mFuture;
  }

  public boolean isDone() {
    return null != mFuture && mFuture.isDone();
  }

}
