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
package com.cs.download.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for downloading operations.
 */
public class DownloadUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtils.class);

  /**
   * Extracts the first cookie from the WebDriver's cookies and formats it as a string.
   *
   * @param driver The WebDriver instance.
   * @return A formatted string representation of the cookie.
   */
  public static String extractCookie(WebDriver driver) {
    Cookie sc = new ArrayList<>(driver.manage().getCookies()).get(0);

    StringBuilder sb = new StringBuilder();

    sb.append(sc.getName()).append("=\"").append(sc.getValue()).append('"');
    if (sc.getPath() != null)
      sb.append(";$Path=\"").append(sc.getPath()).append('"');
    if (sc.getDomain() != null)
      sb.append(";$Domain=\"").append(sc.getDomain()).append('"');

    return sb.toString();
  }

  public static URI silentUri(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      LOGGER.warn("Invalid URI {} {}", uri, e.getMessage());
    }

    return null;
  }

  public static int getFreePort() throws IOException {
    try (ServerSocket s = new ServerSocket()) {
      s.setReuseAddress(true);
      s.bind(new InetSocketAddress((InetAddress) null, 0));
      return s.getLocalPort();
    }
  }
}
