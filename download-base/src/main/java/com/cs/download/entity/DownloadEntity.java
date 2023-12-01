package com.cs.download.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "download")
public class DownloadEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String url;
  private String cookie;
  private int threadCount;
  private String filePath;
  private int status;

  public DownloadEntity() {

  }

  public DownloadEntity(String url, String cookie, int threadCount, String filePath, int status) {
    this.url = url;
    this.cookie = cookie;
    this.threadCount = threadCount;
    this.filePath = filePath;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCookie() {
    return cookie;
  }

  public void setCookie(String cookie) {
    this.cookie = cookie;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "DownloadEntity [mId=" + id + ", mUrl=" + url + ", mCookie=" + cookie + ", mThreadCount=" + threadCount + ", mFilePath=" + filePath
        + ", mStatus=" + status + "]";
  }

}
