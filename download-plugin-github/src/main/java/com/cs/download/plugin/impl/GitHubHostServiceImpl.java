package com.cs.download.plugin.impl;

import java.net.URI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.DownloadUtils;
import com.cs.download.api.plugin.service.host.CrawlRequest;
import com.cs.download.api.plugin.service.host.CrawlResult;
import com.cs.download.api.plugin.service.host.ExtractionRequest;
import com.cs.download.api.plugin.service.host.ExtractionResult;
import com.cs.download.api.plugin.service.host.HandleRequest;
import com.cs.download.api.plugin.service.host.HostService;
import com.cs.download.api.plugin.service.host.LifecycleResult;
import com.cs.download.api.plugin.service.host.LoginRequest;
import com.cs.download.api.plugin.service.host.LoginResult;

public class GitHubHostServiceImpl implements HostService {
  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubHostServiceImpl.class);

  private static final String LOGIN_URL = "http://4kvod.tv/webplayer/";
  private static final String HOST = DownloadUtils.silentUri("https://github.com/").getHost();
  @Override
  public LifecycleResult install() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public LifecycleResult start() {
    return null;
  }

  @Override
  public LifecycleResult stop() {
    return null;
  }

  @Override
  public LifecycleResult status() {
    return null;
  }

  @Override
  public boolean canHandle(HandleRequest request) {

    URI uri = DownloadUtils.silentUri(request.getUri());
    return HOST.equals(uri.getHost());
  }

  @Override
  public LoginResult login(LoginRequest request) {
    LoginResult ret = new LoginResult();

    LOGGER.debug("login");

//    WebDriver driver = request.getDriver();
//    driver.get(LOGIN_URL);
//    driver.findElement(By.cssSelector("#input-login")).sendKeys(request.getUser());
//    driver.findElement(By.cssSelector("#input-pass")).sendKeys(request.getPassword());
//    driver.findElement(By.cssSelector("body > main > div > div > div > form > div:nth-child(5) > button")).click();
//
//    String cookie = DownloadUtils.extractCookie(driver);
//    ret.setCookie(cookie);
//
//    LOGGER.debug("login done cookie {}", cookie);

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
