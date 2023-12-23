package com.cs.download.server.proxy;

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

  public double getBandwidthMbits() {
    return mBandwidthMbits;
  }

  public void setBandwidthMbits(double bandwidthMbits) {
    mBandwidthMbits = bandwidthMbits;
  }

  public boolean isSatisfied(long start, long end, long bytes) {
   double mb = (double) bytes*8 / (double) 1024 / (double) 1024;
      double secs = (double) (end - start) / (double) 1000;
      return mb / secs >= mBandwidthMbits;   
    
  }
}
