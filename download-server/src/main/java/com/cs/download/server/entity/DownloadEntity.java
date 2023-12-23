package com.cs.download.server.entity;

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
  private String filePath;
  private int status;

  public DownloadEntity() {

  }

  public DownloadEntity(String url, String cookie, String filePath, int status) {
    this.url = url;
    this.cookie = cookie;
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
    return "DownloadEntity [mId=" + id + ", mUrl=" + url + ", mCookie=" + cookie + ", mFilePath=" + filePath
        + ", mStatus=" + status + "]";
  }

}
