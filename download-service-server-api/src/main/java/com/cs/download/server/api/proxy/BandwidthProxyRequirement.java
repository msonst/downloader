package com.cs.download.server.api.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BandwidthProxyRequirement implements ProxyRequirement {

  private double mBandwidthMbits;

  public BandwidthProxyRequirement() {
    mBandwidthMbits = 0;

  }

  public BandwidthProxyRequirement(double bandwidthMbits) {
    mBandwidthMbits = bandwidthMbits;
  }

  @JsonIgnore
  @Override
  public boolean isSatisfied(Proxy toCheck) {

    try {
      double bw = download("http://212.183.159.230/10MB.zip", toCheck);

      return bw >= mBandwidthMbits;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  @JsonIgnore
  private boolean isWin() {
    String os = System.getProperty("os.name").toLowerCase();

    if (os.contains("win")) {
      return true;
    } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
      return false;
    } else {
      return false;
    }
  }

  @JsonIgnore
  private double download(String strUrl, Proxy toCheck) throws IOException {

    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection(toCheck);
    connection.setUseCaches(false);

    InputStream inputStream = connection.getInputStream();
    try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        RandomAccessFile randomAccessFile = new RandomAccessFile(isWin() ? "NUL" : "/dev/null", "rw");
        FileChannel nullChannel = randomAccessFile.getChannel()) {

      long start = System.currentTimeMillis();
      long bytes = nullChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      long end = System.currentTimeMillis();

      double mb = (double) bytes*8 / (double) 1024 / (double) 1024;
      double secs = (double) (end - start) / (double) 1000;
      return mb / secs;
    }
  }

  public double getBandwidthMbits() {
    return mBandwidthMbits;
  }

  public void setBandwidthMbits(double bandwidthMbits) {
    mBandwidthMbits = bandwidthMbits;
  }
}
