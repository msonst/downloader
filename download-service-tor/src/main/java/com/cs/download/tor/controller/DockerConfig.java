package com.cs.download.tor.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dockertor")
public class DockerConfig {

  private String mPath;
  private String mRegistryUrl;
  private String mHost;
  private String mWorkDir;
  private String mFile;
  private int mTorPort;
  private int mSshPort;
  private String mSshUser;
  private String mSshPassword;
  private String mTorPassword;
  private int mDockerPort;

  public String getPath() {
    return mPath;
  }

  public String getRegistryUrl() {
    return mRegistryUrl;
  }

  public String getHost() {
    return mHost;
  }

  public String getWorkDir() {
    return mWorkDir;
  }

  public void setPath(String path) {
    mPath = path;
  }

  public void setRegistryUrl(String registryUrl) {
    mRegistryUrl = registryUrl;
  }

  public void setHost(String host) {
    mHost = host;
  }

  public void setWorkDir(String workDir) {
    mWorkDir = workDir;
  }

  public String getFile() {
    return mFile;
  }

  public void setFile(String file) {
    mFile = file;
  }

  public int getTorPort() {
    return mTorPort;
  }

  public void setTorPort(int port) {
    mTorPort = port;
  }

  public int getSshPort() {
    return mSshPort;
  }

  public String getSshUser() {
    return mSshUser;
  }

  public String getSshPassword() {
    return mSshPassword;
  }

  public String getTorPassword() {
    return mTorPassword;
  }

  public void setSshPort(int sshPort) {
    mSshPort = sshPort;
  }

  public void setSshUser(String sshUser) {
    mSshUser = sshUser;
  }

  public void setSshPassword(String sshPassword) {
    mSshPassword = sshPassword;
  }

  public void setTorPassword(String torPassword) {
    mTorPassword = torPassword;
  }

  public int getDockerPort() {
    return mDockerPort;
  }

  public void setDockerPort(int dockerPort) {
    mDockerPort = dockerPort;
  }
  

}
