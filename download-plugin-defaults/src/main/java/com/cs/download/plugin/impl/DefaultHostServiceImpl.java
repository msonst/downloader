package com.cs.download.plugin.impl;

import com.cs.download.api.plugin.service.host.CrawlRequest;
import com.cs.download.api.plugin.service.host.CrawlResult;
import com.cs.download.api.plugin.service.host.ExtractionRequest;
import com.cs.download.api.plugin.service.host.ExtractionResult;
import com.cs.download.api.plugin.service.host.HandleRequest;
import com.cs.download.api.plugin.service.host.HostService;
import com.cs.download.api.plugin.service.host.LoginRequest;
import com.cs.download.api.plugin.service.host.LoginResult;
import com.cs.download.api.plugin.service.host.LifecycleResult;

public class DefaultHostServiceImpl implements HostService {
  @Override
  public LifecycleResult install() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public LifecycleResult start() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LifecycleResult stop() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LifecycleResult status() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean canHandle(HandleRequest request) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public LoginResult login(LoginRequest request) {
    LoginResult ret = new LoginResult();
    return ret;
  }

  @Override
  public CrawlResult crawl(CrawlRequest request) {
    CrawlResult ret = new CrawlResult();
    return ret;
  }

  @Override
  public ExtractionResult extractLinks(ExtractionRequest request) {
    ExtractionResult ret = new ExtractionResult();
    return ret;
  }

}
