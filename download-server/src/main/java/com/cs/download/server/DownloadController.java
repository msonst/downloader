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
package com.cs.download.server;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.download.DownloadManager;
import com.cs.download.DownloadStatusCode;
import com.cs.download.event.DownloadProgressUpdateEvent;
import com.cs.download.event.DownloadStatusListener;
import com.cs.download.event.PartProgressUpdateEvent;
import com.cs.download.server.api.DownloadCommand;
import com.cs.download.server.api.RequestResult;

/**
 * Controller class for handling file downloads.
 *
 * This class is annotated with {@code @RestController}, indicating that it is
 * responsible for handling RESTful requests and returning the appropriate
 * response.
 */
@RestController
public class DownloadController {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private DownloadManager mDownloadManager;
  private final DownloaderConfiguration mConfig;
  private static final DownloadStatusListener LISTENER = new DownloadStatusListener() {

    @Override
    public void onProgress(DownloadProgressUpdateEvent event) {
      LOGGER.debug(event.toString());
    }

    @Override
    public void onProgress(PartProgressUpdateEvent event) {
      LOGGER.debug(event.toString());

    }
  };

  /**
   * Constructor for DownloadController.
   *
   * @param config The configuration for the downloader.
   */
  public DownloadController(DownloaderConfiguration config) {
    mConfig = config;

    // Setting up a proxy for the download manager based on the configuration.
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mConfig.proxyHost(), mConfig.proxyPort()));
    mDownloadManager = new DownloadManager(proxy, mConfig.outpath(), mConfig.parallelDownloads());
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
  public RequestResult add(@RequestParam(value = "url", defaultValue = "") String url,
      @RequestParam(value = "cookie", defaultValue = "") String cookie) {

    RequestResult ret;

    try {
      // Initiating the download using the DownloadManager.
      ret = new RequestResult(mDownloadManager.addDownload(new URL(url.trim().replace("\"", "")), cookie, 1, LISTENER), DownloadStatusCode.OK);
    } catch (Exception e) {
      // Handling exceptions and creating a DownloadResult with an error.
      ret = new RequestResult(null, DownloadStatusCode.ERROR.setMessage(e.getMessage()));
    }

    // Logging the details of the download request.
    LOGGER.debug("REST download called url={} cookie={} ret={}", url, cookie, ret);

    return ret;
  }

  @GetMapping(value = "/status")
  public RequestResult status(@RequestParam(value = "downloadId", defaultValue = "") UUID downloadId) {

    LOGGER.debug("REST status called downloadId={}", downloadId);

    RequestResult ret = new RequestResult(downloadId, mDownloadManager.getStatus(downloadId));

    LOGGER.debug("REST status called downloadId={} ret={}", downloadId, ret);

    return ret;
  }

  @GetMapping(value = "/control")
  public RequestResult control(@RequestParam(value = "downloadId", defaultValue = "") UUID downloadId,
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

}
