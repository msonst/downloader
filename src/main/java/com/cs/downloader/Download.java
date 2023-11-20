package com.cs.downloader;

import java.util.UUID;

public class Download {

	private UUID mDownloadId;

	public Download(UUID id) {
		mDownloadId = id;
	}

	public UUID getDownloadId() {
		return mDownloadId;
	}
}
