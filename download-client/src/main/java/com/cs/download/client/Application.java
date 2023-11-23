package com.cs.download.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.server.api.DownloadCommand;
import com.cs.download.server.api.RequestResult;

import io.restassured.RestAssured;

public class Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    RequestResult result = RestAssured.given().param("url", "https://github.com/jamesward/play-load-tests/raw/master/public/10mb.txt")
        .param("cookie", "cookie").get("http://localhost:9090/add").as(RequestResult.class);

    LOGGER.debug("add {}", result);

    result = RestAssured.given().param("downloadId", result.downloadId()).get("http://localhost:9090/status").as(RequestResult.class);
    LOGGER.debug("add {}", result);

    result = RestAssured.given().param("downloadId", result.downloadId()).param("cmd", DownloadCommand.START).get("http://localhost:9090/control")
        .as(RequestResult.class);
    LOGGER.debug("start {}", result);

  }

}
