package com.cs.download.server.api.proxy;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;

public class ProxyResult {

  private List<Proxy> mProxies;
  private long mTimestamp;
  private Exception mException;

  public ProxyResult(long timestamp, List<Proxy> proxies) {
    mTimestamp = timestamp;
    mProxies = proxies;
  }

  public ProxyResult(Exception exception) {
    mException = exception;
  }

  public List<Proxy> getProxies() {
    return mProxies;
  }

  public long getTimestamp() {
    return mTimestamp;
  }
  
}
