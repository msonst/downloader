package com.cs.download.tor.controller;

import java.util.HashMap;
import java.util.Map;

public class DockerTorResult {

  private String mImageId;
  private String mContainerId;
  private Map<String, Map<Integer, Integer>> mMapping = new HashMap<String, Map<Integer, Integer>>();

  public DockerTorResult(String imageId) {
    mImageId = imageId;
  }

  public String getImageId() {
    return mImageId;
  }

  public void setImageId(String imageId) {
    mImageId = imageId;
  }

  public String getContainerId() {
    return mContainerId;
  }

  public void setContainerId(String containerId) {
    mContainerId = containerId;
  }

  public Map<String, Map<Integer, Integer>> getMapping() {
    return mMapping;
  }

  public void setMapping(Map<String, Map<Integer, Integer>> mapping) {
    mMapping = mapping;
  }

}