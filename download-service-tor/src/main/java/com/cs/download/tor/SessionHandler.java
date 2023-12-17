package com.cs.download.tor;

public interface  SessionHandler {

  public <T> T onConnected(String localHost, int localPort);

  public void onError(Exception exception);

}
