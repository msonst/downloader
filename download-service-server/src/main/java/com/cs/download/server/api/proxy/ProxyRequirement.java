package com.cs.download.server.api.proxy;

import java.net.Proxy;

public interface ProxyRequirement {

  public boolean isSatisfied(Proxy toCheck);
}
