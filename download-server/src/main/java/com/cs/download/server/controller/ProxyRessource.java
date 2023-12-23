package com.cs.download.server.controller;

import java.net.Proxy;

import com.cs.download.server.proxy.BandwidthProxyRequirement;

public class ProxyRessource{

  private Proxy mProxy;
  private BandwidthProxyRequirement mBandwidthProxyRequirement;

  public ProxyRessource(Proxy proxy, BandwidthProxyRequirement bandwidthProxyRequirement) {
    setProxy(proxy);
    setBandwidthProxyRequirement(bandwidthProxyRequirement);
  }

  public boolean isFree() {
    return true;
  }

  public Proxy getProxy() {
    return mProxy;
  }

  public void setProxy(Proxy proxy) {
    mProxy = proxy;
  }

  public BandwidthProxyRequirement getBandwidthProxyRequirement() {
    return mBandwidthProxyRequirement;
  }

  public void setBandwidthProxyRequirement(BandwidthProxyRequirement bandwidthProxyRequirement) {
    mBandwidthProxyRequirement = bandwidthProxyRequirement;
  }
  
}
