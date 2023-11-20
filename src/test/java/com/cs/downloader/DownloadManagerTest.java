/*
 * Copyright 2023 Michael Sonst @ https://www.corporate-startup.com Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.cs.downloader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
* The {@code DownloadManagerTest} class contains unit tests for the {@link DownloadManager} class.
*
* @see DownloadManager
*/
class DownloadManagerTest {

 // Constants for test data
 private static final char[] TEST_CONTENT = "0123456789".toCharArray();
 private static final String COOKIE = "C11=\"v1\";$Path=\"some/path\"";
 private static final String SAVE_PATH = "./appdata/";

 // Mock objects and other attributes
 private AtomicBoolean mCloseStream;
 private Proxy mProxy;

 // Setup performed before each test
 @BeforeEach
 void setUp() throws Exception {
     cleanUpFiles();
     mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.100", 8118));

     mCloseStream = new AtomicBoolean(true);
 }

 // Helper method to delete test files
 private void cleanUpFiles() {
     new File(SAVE_PATH).delete();
     new File(SAVE_PATH + ".0").delete();
 }

 // Cleanup performed after each test
 @AfterEach
 void tearDown() throws Exception {
     cleanUpFiles();
 }

 /**
  * Helper method to create a mock URL with a mock HttpURLConnection.
  *
  * @param proxy The proxy to use for the mock URL.
  * @return The mock URL object.
  */
 private URL mockUrl(Proxy proxy) {
     try {
         HttpURLConnection mockHttpURLConnection = mock(HttpURLConnection.class);
         when(mockHttpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

         URL mockURL = mock(URL.class);
         when(mockURL.openConnection(any())).thenReturn(mockHttpURLConnection);
         when(mockURL.openConnection(notNull())).thenReturn(null);
         when(mockURL.openConnection()).thenReturn(mockHttpURLConnection);
         when(mockHttpURLConnection.getContentLengthLong()).thenReturn((long) TEST_CONTENT.length);
         when(mockHttpURLConnection.getResponseCode()).thenReturn(200);
         when(mockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {
             int idx = 0;

             @Override
             public int read() throws IOException {
                 int ret = 0;
                 if (mCloseStream.get() && idx >= 5) {
                     close();
                     ret = -1;
                 } else
                     ret = TEST_CONTENT[idx++ % (TEST_CONTENT.length)];
                 return ret;
             }
         });

         return mockURL;
     } catch (IOException e) {
         e.printStackTrace();
         return null;
     }
 }

 /**
  * A unit test for the {@link DownloadManager} class.
  */
 @Test
 void test() {
     // Initialize DownloadManager with a proxy, save path, and maximum parallel downloads
     DownloadManager uut = new DownloadManager(mProxy, SAVE_PATH, 1);

     // Create an array to store downloads
     Download[] downloads = new Download[10];
     for (int i = 0; i < 10; i++) {
         // Add a download task with a mock URL and cookie
         downloads[i] = uut.addDownload(mockUrl(mProxy), COOKIE);
         
         // Start the download task
         uut.start(downloads[i]);
     }

     // Check if the first download is complete
     uut.isComplete(downloads[0]);

     // Wait for all downloads to complete
     uut.waitForCompletion();
 }
}
