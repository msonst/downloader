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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BandwidthProxyRequirement implements ProxyRequirement {
  private static final Logger LOGGER = LoggerFactory.getLogger(BandwidthProxyRequirement.class);

  private double mBandwidthMbits;

  public BandwidthProxyRequirement(double bandwidthMbits) {
    mBandwidthMbits = bandwidthMbits;
  }

  @Override
  public boolean isSatisfied(Proxy toCheck) {
    long start = System.currentTimeMillis();

    try {
      double mb = download("http://212.183.159.230/10MB.zip", toCheck)/ (double)  1024 / (double)  1024;
      double secs = (double) (System.currentTimeMillis() - start) / (double)1000;
      double bw = mb /  secs;
      
      //LOGGER.debug
      LOGGER.debug("Bandwidth "+ bw);
      
      return bw >= mBandwidthMbits;
    } catch (IOException e) {
    }

    return false;
  }

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

  private long download(String strUrl, Proxy toCheck) throws IOException {

    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection(toCheck);

    InputStream inputStream = connection.getInputStream();
    try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        RandomAccessFile randomAccessFile = new RandomAccessFile(isWin() ? "NUL" : "/dev/null", "rw");
        FileChannel nullChannel = randomAccessFile.getChannel()) {

      return nullChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
  }
}
