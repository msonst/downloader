package com.cs.download.server.api.proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyRequest {

  private List<ProxyRequirement> mProxyRequirements = new ArrayList<ProxyRequirement>();

  public ProxyRequest() {
  }

  public List<ProxyRequirement> getProxyRequirements() {
    return mProxyRequirements;
  }

  public void addProxyRequirement(ProxyRequirement proxyRequirement) {
    mProxyRequirements.add(proxyRequirement);
  }

}
