package com.cs.download.plugin.impl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.download.api.plugin.service.host.LifecycleResult;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.api.plugin.service.proxy.ProxyConfig;
import com.cs.download.api.plugin.service.proxy.ProxyProviderService;
import com.cs.download.api.plugin.service.proxy.ProxyRequest;
import com.cs.download.api.plugin.service.proxy.ProxyRequirement;
import com.cs.download.api.plugin.service.proxy.ProxyResult;
import com.cs.download.tor.TorCtl;
@Component
public class TorCtlServiceImpl implements ProxyProviderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TorCtlServiceImpl.class);

  private LifecycleState mState;
  
  private @Autowired TorCtl mTorCtl;
  private @Autowired ProxyConfig mProxyConfig;
  
  @Override
  public LifecycleResult install() {
    return new LifecycleResult(mState);
  }
  @Override
  public LifecycleResult start() {
    mState = LifecycleState.RUNNING;
    return new LifecycleResult(mState);
  }

  @Override
  public LifecycleResult stop() {
    mState = LifecycleState.STOPPED;
    return new LifecycleResult(mState);
  }

  @Override
  public LifecycleResult status() {
    return new LifecycleResult(mState);
  }

  @Override
  public ProxyResult next(ProxyRequest request) {
    int sshPort = mProxyConfig.getSsh().getPort();
    String sshUser = mProxyConfig.getSsh().getUser();
    String sshPassword = mProxyConfig.getSsh().getPassword();
    String torPass = mProxyConfig.getTor().getPassword();
    String torHost = mProxyConfig.getTor().getHost();
    int torPort = mProxyConfig.getTor().getPort();
    int torCtlPort = mProxyConfig.getTor().getCtlPort();
    List<ProxyRequirement> requirements = request.getProxyRequirements();

    try {
      boolean satisfied = false;
      do {
        mTorCtl.changeIp(sshUser, sshPassword, sshPort, torHost, torCtlPort, torPort, torPass);
      } while (!(satisfied = isSatisfied(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(torHost, torPort)), requirements)));

      if (!satisfied)
        throw new Exception("Requirements not satisfied");

    } catch (Exception e) {
      return new ProxyResult(e);
    }
    return new ProxyResult(System.currentTimeMillis(),
        Arrays.asList(new Proxy[] { new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(torHost, torPort)) }));
  }

  private boolean isSatisfied(Proxy proxy, List<ProxyRequirement> requirements) {
    return requirements.stream().filter(r -> !r.isSatisfied(proxy)).collect(Collectors.toList()).isEmpty();
  }



}
