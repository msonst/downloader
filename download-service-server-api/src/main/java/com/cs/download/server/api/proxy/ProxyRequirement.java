package com.cs.download.server.api.proxy;

import java.net.Proxy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = BandwidthProxyRequirement.class, name = "BandwidthProxyRequirement") })
public interface ProxyRequirement {

  public boolean isSatisfied(Proxy toCheck);
}
