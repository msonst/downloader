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
package com.cs.downloader.event;

public class PartProgressUpdateEvent extends DownloadEvent {

	private static final long serialVersionUID = -6472846506275050177L;
	private Object mSource;
	private int mPartId;
	private long mStartRange;
	private long mEndRange;
	private long mBytes;
	private long mBytesSinceStart;
	private long mFileSizeAtStart;
	private long mCycleStart;
	private long mCycleEnd;

	public PartProgressUpdateEvent(Object source, int partId, long startRange, long endRange, long bytes, long bytesSinceStart,
			long fileSizeAtStart, long cycleStart, long cycleEnd) {
		super(source);
		mSource = source;
		mPartId = partId;
		mStartRange = startRange;
		mEndRange = endRange;
		mBytes = bytes;
		mBytesSinceStart = bytesSinceStart;
		mFileSizeAtStart = fileSizeAtStart;
		mCycleStart = cycleStart;
		mCycleEnd = cycleEnd;
	}

	public int getPartId() {
		return mPartId;
	}

	public long getStartRange() {
		return mStartRange;
	}

	public long getEndRange() {
		return mEndRange;
	}

	public long getBytes() {
		return mBytes;
	}

	public long getBytesSinceStart() {
		return mBytesSinceStart;
	}

	public long getFileSizeAtStart() {
		return mFileSizeAtStart;
	}

	public long getCycleStart() {
		return mCycleStart;
	}

	public long getCycleEnd() {
		return mCycleEnd;
	}

	@Override
	public String toString() {
		return "PartProgressUpdateEvent [mSource=" + mSource + ", mPartId=" + mPartId + ", mStartRange=" + mStartRange + ", mEndRange=" + mEndRange
				+ ", mBytes=" + mBytes + ", mBytesSinceStart=" + mBytesSinceStart + ", mFileSizeAtStart="
				+ mFileSizeAtStart + ", mCycleStart=" + mCycleStart + ", mCycleEnd=" + mCycleEnd + "]";
	}

}