package com.cs.download.server.api.host;

import org.openqa.selenium.WebDriver;

public class LoginRequest {

  private String mUrl;
  private String mUser;
  private String mPassword;

  public LoginRequest(String url, String user, String password) {
    mUrl = url;
    mUser = user;
    mPassword = password;
  }

  public WebDriver getDriver() {
    //refer to driver provider
    
    return null;
  }

  public CharSequence getUser() {
    // TODO Auto-generated method stub
    return null;
  }

  public CharSequence getPassword() {
    // TODO Auto-generated method stub
    return null;
  }

}
