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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cs.download.DownloadStatusCode;
import com.cs.download.ResumableDownload;
import com.cs.download.event.DownloadProgressUpdateEvent;
import com.cs.download.event.DownloadStatusListener;
import com.cs.download.event.PartProgressUpdateEvent;

/**
 * Tests for the ResumableDownload class.
 */
class ResumableDownloadTest {

  // Constants for test data
  private static final char[] TEST_CONTENT = "0123456789".toCharArray();
  private static final String COOKIE = "C11=\"v1\";$Path=\"some/path\"";
  private static final String SAVE_PATH = "./appdata/test.mp4";

  // Mock objects and other attributes
  private URL mMockURL;
  private Proxy mProxy;
  private HttpURLConnection mMockHttpURLConnection;

  // Setup performed before each test
  @BeforeEach
  void setUp() throws Exception {
    cleanUpFiles();
    mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.100", 8118));
    mMockHttpURLConnection = mock(HttpURLConnection.class);
    when(mMockHttpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
    mMockURL = mock(URL.class);
    when(mMockURL.openConnection(any())).thenReturn(mMockHttpURLConnection);
  }

  // Cleanup performed after each test
  @AfterEach
  void tearDown() throws Exception {
    cleanUpFiles();
  }

  // Helper method to delete test files
  private void cleanUpFiles() {
    if (!new File(SAVE_PATH).delete() || !new File(SAVE_PATH + ".0").delete())
      System.out.println("File not present");
  }

  private boolean verifyFile(long length, File file, char[] pattern) throws IOException {

    boolean ret = true;
    ret &= file.exists();
    ret &= file.length() == length;

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < file.length() - pattern.length; i += pattern.length) {
      sb.append(pattern);
    }

    for (int i = 0; sb.length() < length; i++) {
      sb.append(pattern[i % pattern.length]);
    }
    String shouldPattern = sb.toString();

    String content = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
    ret &= content.length() == shouldPattern.length();
    ret &= shouldPattern.equals(content);

    return ret;
  }

  /**
   * Tests the downloadSync method of ResumableDownload with an invalid content
   * length.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testDownloadInvalidContentLength() throws IOException, InterruptedException, ExecutionException {

    int numThreads = 1;

    when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(Long.MIN_VALUE);
    DownloadStatusCode status = new ResumableDownload(numThreads, mProxy, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    Assertions.assertTrue(status != DownloadStatusCode.COMPLETE);
  }

  /**
   * Tests the downloadSync method of ResumableDownload with an HTTP error code.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testDownloadErrorCode() throws IOException, InterruptedException, ExecutionException {

    int numThreads = 1;

    when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(Long.MIN_VALUE);
    when(mMockHttpURLConnection.getResponseCode()).thenReturn(404);

    DownloadStatusCode status = new ResumableDownload(numThreads, mProxy, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    Assertions.assertTrue(status == DownloadStatusCode.NOT_FOUND);
  }

  /**
   * Tests that downloadSync uses a proxy when provided.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testDownloadUsesProxyWhenProvided() throws IOException, InterruptedException, ExecutionException {

    int numThreads = 1;

    when(mMockHttpURLConnection.getContentLengthLong()).thenReturn((long) 100);
    when(mMockHttpURLConnection.getResponseCode()).thenReturn(200);
    when(mMockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {

      @Override
      public int read() throws IOException {
        return 0;
      }
    });

    new ResumableDownload(numThreads, mProxy, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    // not allowed without using proxy
    verify(mMockURL, times(0)).openConnection();
  }

  /**
   * Tests that downloadSync does not use a proxy when not provided.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testDownloadUsesNoProxyWhenNotProvided() throws IOException, InterruptedException, ExecutionException {

    int numThreads = 1;

    when(mMockURL.openConnection(notNull())).thenReturn(null);
    when(mMockURL.openConnection()).thenReturn(mMockHttpURLConnection);
    when(mMockHttpURLConnection.getContentLengthLong()).thenReturn((long) 8 * 1024 * 1024);
    when(mMockHttpURLConnection.getResponseCode()).thenReturn(200);
    when(mMockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {
      int idx = 0;

      @Override
      public int read() throws IOException {
        return TEST_CONTENT[idx++ % (TEST_CONTENT.length)];
      }
    });

    new ResumableDownload(numThreads, null, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    // not allowed with proxy
    verify(mMockURL, times(0)).openConnection(notNull());
  }

  /**
   * Tests that downloadSync can resume a download.
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  void testDownloadResume() throws IOException, InterruptedException, ExecutionException {

    int numThreads = 1;
    final AtomicBoolean closeStream = new AtomicBoolean(true);

    when(mMockURL.openConnection(notNull())).thenReturn(null);
    when(mMockURL.openConnection()).thenReturn(mMockHttpURLConnection);
    when(mMockHttpURLConnection.getContentLengthLong()).thenReturn((long) TEST_CONTENT.length);
    when(mMockHttpURLConnection.getResponseCode()).thenReturn(200);
    when(mMockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {
      int idx = 0;

      @Override
      public int read() throws IOException {
        int ret = 0;
        if (closeStream.get() && idx >= 5) {
          close();
          ret = -1;
        } else
          ret = TEST_CONTENT[idx++ % (TEST_CONTENT.length)];
        return ret;
      }
    });

    DownloadStatusCode status = new ResumableDownload(numThreads, null, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    Assertions.assertTrue(status == DownloadStatusCode.ERROR);

    closeStream.set(false);

    status = new ResumableDownload(numThreads, null, new DownloadStatusListener() {
      @Override
      public void onProgress(PartProgressUpdateEvent event) {
      }

      @Override
      public void onProgress(DownloadProgressUpdateEvent event) {
      }
    }).downloadSync(mMockURL, SAVE_PATH, COOKIE);

    Assertions.assertTrue(status == DownloadStatusCode.COMPLETE);

    File file = new File(SAVE_PATH);
    Assertions.assertTrue(file.exists());

    Assertions.assertTrue(verifyFile(TEST_CONTENT.length, new File(SAVE_PATH), TEST_CONTENT));
  }

  // @Test
  // void testDownloadFileLargerThanSizePerRead() throws IOException,
  // InterruptedException, ExecutionException {
  // long length = (10 * 1024 * 1024) + 2;
  // int numThreads = 1;
  //
  // when(mMockURL.openConnection(notNull())).thenReturn(null);
  // when(mMockURL.openConnection()).thenReturn(mMockHttpURLConnection);
  // when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(length);
  // when(mMockHttpURLConnection.getResponseCode()).thenReturn(200);
  // when(mMockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {
  // int idx = 0;
  //
  // @Override
  // public int read() throws IOException {
  // return TESTCONTENT[idx++ % (TESTCONTENT.length)];
  // }
  // });
  //
  // DownloadStatusCode status = new ResumableDownload(numThreads, null, new
  // DownloadStatusListener() {
  // @Override
  // public void onPartProgress(PartProgressUpdateEvent event) {
  // }
  // }).downloadSync(mMockURL, SAVEPATH, COOKIE);
  //
  // Assertions.assertTrue(status == DownloadStatusCode.COMPLETE);
  // Assertions.assertTrue(verifyFile(length, new File(SAVEPATH), TESTCONTENT));
  // }
}
