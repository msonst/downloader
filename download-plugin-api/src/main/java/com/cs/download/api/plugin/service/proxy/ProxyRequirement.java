package com.cs.download.api.plugin.service.proxy;

import java.net.Proxy;

public interface ProxyRequirement {

  public boolean isSatisfied(Proxy toCheck);
}
