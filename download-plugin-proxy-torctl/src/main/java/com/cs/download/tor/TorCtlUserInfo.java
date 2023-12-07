package com.cs.download.tor;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class TorCtlUserInfo implements UserInfo, UIKeyboardInteractive {

  private final String mPassword;

  public TorCtlUserInfo(String password) {
    mPassword = password;
  }

  @Override
  public String getPassphrase() {
    return null;
  }

  @Override
  public String getPassword() {
    return mPassword;
  }

  @Override
  public boolean promptPassword(String message) {
    return true;
  }

  @Override
  public boolean promptPassphrase(String message) {
    return false;
  }

  @Override
  public boolean promptYesNo(String message) {
    return true;
  }

  @Override
  public void showMessage(String message) {
    // ignored
  }

  @Override
  public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
    return new String[] { mPassword };
  }
}