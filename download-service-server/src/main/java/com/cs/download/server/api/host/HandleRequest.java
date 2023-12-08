package com.cs.download.server.api.host;

public class HandleRequest {

  private String mURI;

  public HandleRequest(String uri) {
    mURI = uri;
  }

  public String getUri() {
    return mURI;
  }

}
