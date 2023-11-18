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

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;

import com.cs.downloader.event.DownloadStatusListener;
import com.cs.downloader.event.PartProgressUpdateEvent;

class ResumableDownloadTest {
	private static final char[] TESTCONTENT = "0123456789".toCharArray();
	private static final String COOKIE = "C11=\"v1\";$Path=\"some/path\"";
	private URL mMockURL;
	private Proxy mProxy;
	private HttpURLConnection mMockHttpURLConnection;
	private static final String SAVEPATH = "./appdata/test.mp4";

	@BeforeEach
	void setUp() throws Exception {
		new File(SAVEPATH).delete();
		new File(SAVEPATH + ".0").delete();

		mProxy = new Proxy(Proxy.Type.HTTP, new java.net.InetSocketAddress("192.168.0.100", 8118));

		mMockHttpURLConnection = mock(HttpURLConnection.class);
		when(mMockHttpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

		mMockURL = mock(URL.class);
		Mockito.when(mMockURL.openConnection(Mockito.notNull())).thenReturn(mMockHttpURLConnection);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		new File(SAVEPATH).delete();
		new File(SAVEPATH + ".0").delete();
	}
	
	private boolean verifyFile(long length, File file, char[] pattern) {

		boolean ret = true;
		ret &= file.exists();
		ret &= file.length() == length;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < file.length() - pattern.length; i += pattern.length) {
			sb.append(pattern);
		}

		for (int i = 0; sb.length() < length; i++) {
			sb.append(pattern[i % pattern.length]);
		}
		String shouldPattern = sb.toString();

		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			ret &= content.length() == shouldPattern.length();
			ret &= shouldPattern.equals(content);
		} catch (IOException e) {
			e = null;
			ret = false;
		}

		return ret;
	}

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

	@Test
	void testDownloadInvalidContentLength() throws IOException, InterruptedException, ExecutionException {

		int numThreads = 1;

		when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(Long.MIN_VALUE);
		DownloadStatusCode status = new ResumableDownload(numThreads, mProxy, new DownloadStatusListener() {
			@Override
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		Assertions.assertTrue(status != DownloadStatusCode.COMPLETE);
	}

	@Test
	void testDownloadErrorCode() throws IOException, InterruptedException, ExecutionException {

		int numThreads = 1;

		when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(Long.MIN_VALUE);
		when(mMockHttpURLConnection.getResponseCode()).thenReturn(404);

		DownloadStatusCode status = new ResumableDownload(numThreads, mProxy, new DownloadStatusListener() {
			@Override
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		Assertions.assertTrue(status == DownloadStatusCode.NOT_FOUND);
	}

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
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		// not allowed without using proxy
		verify(mMockURL, times(0)).openConnection();
	}

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
				return TESTCONTENT[idx++ % (TESTCONTENT.length)];
			}
		});

		new ResumableDownload(numThreads, null, new DownloadStatusListener() {
			@Override
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		// not allowed with proxy
		verify(mMockURL, times(0)).openConnection(notNull());
	}

	@Test
	void testDownloadResume() throws IOException, InterruptedException, ExecutionException {

		int numThreads = 1;
		final AtomicBoolean closeStream = new AtomicBoolean(true);

		when(mMockURL.openConnection(notNull())).thenReturn(null);
		when(mMockURL.openConnection()).thenReturn(mMockHttpURLConnection);
		when(mMockHttpURLConnection.getContentLengthLong()).thenReturn((long) TESTCONTENT.length);
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
					ret = TESTCONTENT[idx++ % (TESTCONTENT.length)];
				return ret;
			}
		});

		DownloadStatusCode status = new ResumableDownload(numThreads, null, new DownloadStatusListener() {
			@Override
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		Assertions.assertTrue(status == DownloadStatusCode.ERROR);

		closeStream.set(false);

		status = new ResumableDownload(numThreads, null, new DownloadStatusListener() {
			@Override
			public void onPartProgress(PartProgressUpdateEvent event) {
			}
		}).downloadSync(mMockURL, SAVEPATH, COOKIE);

		Assertions.assertTrue(status == DownloadStatusCode.COMPLETE);

		File file = new File(SAVEPATH);
		Assertions.assertTrue(file.exists());

		Assertions.assertTrue(verifyFile(TESTCONTENT.length, new File(SAVEPATH), TESTCONTENT));
	}

//	@Test
//	void testDownloadFileLargerThanSizePerRead() throws IOException, InterruptedException, ExecutionException {
//		long length = (10 * 1024 * 1024) + 2;
//		int numThreads = 1;
//
//		when(mMockURL.openConnection(notNull())).thenReturn(null);
//		when(mMockURL.openConnection()).thenReturn(mMockHttpURLConnection);
//		when(mMockHttpURLConnection.getContentLengthLong()).thenReturn(length);
//		when(mMockHttpURLConnection.getResponseCode()).thenReturn(200);
//		when(mMockHttpURLConnection.getInputStream()).thenReturn(new InputStream() {
//			int idx = 0;
//
//			@Override
//			public int read() throws IOException {
//				return TESTCONTENT[idx++ % (TESTCONTENT.length)];
//			}
//		});
//
//		DownloadStatusCode status = new ResumableDownload(numThreads, null, new DownloadStatusListener() {
//			@Override
//			public void onPartProgress(PartProgressUpdateEvent event) {
//			}
//		}).downloadSync(mMockURL, SAVEPATH, COOKIE);
//
//		Assertions.assertTrue(status == DownloadStatusCode.COMPLETE);
//		Assertions.assertTrue(verifyFile(length, new File(SAVEPATH), TESTCONTENT));
//	}
}
