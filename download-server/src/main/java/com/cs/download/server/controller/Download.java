package com.cs.download.server.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.cs.download.server.api.DownloadStatusCode;

public class Download {

  private Callable mCallable;
  private Future<DownloadStatusCode> mFuture;

  public Download(Callable callable) {
    mCallable = callable;
  }

  public Callable getCallable() {
    return mCallable;
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
