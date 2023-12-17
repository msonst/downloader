/*
 *     Copyright 2023 Michael Sonst @ https://www.corporate-startup.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cs.download.controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs.download.ServerApplication;
import com.cs.download.server.api.DownloadCommand;
import com.cs.download.server.api.DownloadStatusCode;
import com.cs.download.server.api.RequestResult;

/**
 * Controller class for handling file downloads.
 *
 * This class is annotated with {@code @RestController}, indicating that it is
 * responsible for handling RESTful requests and returning the appropriate
 * response.
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);
  @Autowired
  private DownloadManager mDownloadManager;

  /**
   * Constructor for DownloadController.
   *
   * @param config The configuration for the downloader.
   */
  public DownloadController() {
    // Setting up a proxy for the download manager based on the configuration.
    //    Proxy proxy = new Proxy(Proxy.Type.valueOf(config.getProxyType()), new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
  }

  /**
   * Handles the HTTP GET request to initiate a file download.
   *
   * @param url    The URL of the file to be downloaded.
   * @param cookie The optional cookie to be used in the download request.
   * @return A {@code DownloadResult} object containing information about the
   *         download.
   */
  @SuppressWarnings("deprecation")
  @GetMapping("/add")
  public RequestResult add(@RequestParam(value = "url") String url, @RequestParam(value = "cookie") String cookie) {

    RequestResult ret;

    try {
      // Initiating the download using the DownloadManager.
      ret = new RequestResult(mDownloadManager.addDownload(url.trim().replace("\"", ""), cookie), DownloadStatusCode.OK);
    } catch (

    Exception e) {
      // Handling exceptions and creating a DownloadResult with an error.
      ret = new RequestResult(null, DownloadStatusCode.ERROR.setMessage(e.getMessage()));
    }

    // Logging the details of the download request.
    LOGGER.debug("REST download called url={} cookie={} ret={}", url, cookie, ret);

    return ret;
  }

  @GetMapping(value = "/status")
  public RequestResult status(@RequestParam(value = "downloadId", defaultValue = "") Long downloadId) {

    LOGGER.debug("REST status called downloadId={}", downloadId);

    RequestResult ret = new RequestResult(downloadId, mDownloadManager.getStatus(downloadId));

    LOGGER.debug("REST status called downloadId={} ret={}", downloadId, ret);

    return ret;
  }

  @GetMapping(value = "/control")
  public RequestResult control(@RequestParam(value = "downloadId", defaultValue = "") Long downloadId,
      @RequestParam(value = "cmd", defaultValue = "") DownloadCommand command) {

    LOGGER.debug("REST status called downloadId={}, command={}", downloadId, command);
    RequestResult ret = null;

    switch (command) {
      case DELETE:
        break;
      case START:
        ret = new RequestResult(downloadId, mDownloadManager.start(downloadId));
        break;
      case STOP:
        break;
      default:
        break;
    }

    LOGGER.debug("REST status called downloadId={} ret={}", downloadId, ret);

    return ret;
  }

  @GetMapping("/files")
  @ResponseBody
  public List<String> listFolder() {
    return Arrays.stream(mDownloadManager.listFiles()).map(File::getName).collect(Collectors.toList());
  }
}
