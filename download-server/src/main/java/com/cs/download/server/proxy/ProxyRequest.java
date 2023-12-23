package com.cs.download.server.proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyRequest {

  private List<ProxyRequirement> mProxyRequirements = new ArrayList<ProxyRequirement>();
  private String mId;

  public ProxyRequest() {
  }

  public List<ProxyRequirement> getProxyRequirements() {
    return mProxyRequirements;
  }

  public void addProxyRequirement(ProxyRequirement proxyRequirement) {
    mProxyRequirements.add(proxyRequirement);
  }

  public void setProxyRequirements(List<ProxyRequirement> proxyRequirements) {
    mProxyRequirements = proxyRequirements;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

}
