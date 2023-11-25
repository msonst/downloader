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
package com.cs.downloader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

import com.cs.download.DownloadUtils;

/**
 * Tests for the ResumableDownload class.
 */
class ResumableDownloadTest {
  private static final String COOKIE = "C11=\"v1\";$Path=\"some/path\"";

  // Setup performed before each test
  @BeforeEach
  void setUp() throws Exception {
  }

  // Cleanup performed after each test
  @AfterEach
  void tearDown() throws Exception {
  }

  /**
   * Tests the extractCookie method of DownloadUtils.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testExtractCookie() throws IOException, InterruptedException, ExecutionException {
    Set<Cookie> cookies = new HashSet<Cookie>();
    cookies.add(new Cookie("C11", "v1", "some/path"));

    WebDriver mockDriver = mock(WebDriver.class);
    Options mockOptions = mock(Options.class);
    when(mockDriver.manage()).thenReturn(mockOptions);
    when(mockOptions.getCookies()).thenReturn(cookies);

    String cookie = DownloadUtils.extractCookie(mockDriver);
    Assertions.assertEquals(cookie, COOKIE);
  }
}
