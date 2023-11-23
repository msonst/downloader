package com.cs.downloader.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.cs.download.server.Application;
import com.cs.download.server.api.RequestResult;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class DownloadControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Test
  public void givenNoAuthentication_whenAccessHome_thenUnauthorized() {
    int statusCode = RestAssured.get("http://localhost:" + port).statusCode();
    assertEquals(HttpStatus.NOT_FOUND.value(), statusCode);
  }

  @Test
  public void givenDownload_whenInvalidUrl_thenDownloadStatusStarted() {
    RequestResult result = RestAssured.given().param("url", "url").param("cookie", "cookie").get("http://localhost:" + port + "/add")
        .as(RequestResult.class);
    assertNotNull(result.status());
    assertNotNull(result.status().getMessage());
  }

  @Test
  public void givenDownload_whenRequest_thenDownloadStatusStarted() {

    RequestResult result = RestAssured.given().param("url", "https://github.com/jamesward/play-load-tests/raw/master/public/10mb.txt")
        .param("cookie", "cookie").get("http://localhost:" + port + "/add").as(RequestResult.class);
    assertNotNull(result.downloadId());
    assertNotNull(result.status());

    RequestSpecification param = RestAssured.given().param("downloadId", result.downloadId());

    RequestResult status = param.get("http://localhost:" + port + "/status").as(RequestResult.class);
    assertNotNull(status.downloadId());
    assertNotNull(status.status());
  }

  //	    @Test
  //	    public void givenAuthentication_whenAccessHome_thenOK() {
  //	        int statusCode = RestAssured.given().auth().basic("john", "123").get("http://localhost:" + port).statusCode();
  //	        assertEquals(HttpStatus.OK.value(), statusCode);
  //	    }
}
