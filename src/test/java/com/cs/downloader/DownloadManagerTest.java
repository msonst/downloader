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
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	private URL mockUrl(Proxy proxy) {
		URL mockURL = null;
		try {

			HttpURLConnection mockHttpURLConnection;

			mockHttpURLConnection = mock(HttpURLConnection.class);
			when(mockHttpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

			mockURL = mock(URL.class);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mockURL;
	}

	// Cleanup performed after each test
	@AfterEach
	void tearDown() throws Exception {
		cleanUpFiles();
	}

	// Helper method to delete test files
	private void cleanUpFiles() {
		new File(SAVE_PATH).delete();
		new File(SAVE_PATH + ".0").delete();
	}

	private boolean verifyFile(long length, File file, char[] pattern) {

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
	void test() {
		DownloadManager uut = new DownloadManager(mProxy, SAVE_PATH, 1);

		
		Download[]downloads =new Download[10];
		for (int i = 0; i < 10; i++) {
			downloads[i] = uut.addDownload(mockUrl(mProxy), COOKIE);
			
			uut.start(downloads[i]);
		}
		//uut.startAll();
		
		uut.isComplete(downloads[0]);

		uut.waitForCompletion();
	}

}
