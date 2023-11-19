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
package com.cs.downloader.event;

/**
 * An event class representing the progress update of a download part.
 */
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

    /**
     * Constructs a new PartProgressUpdateEvent.
     *
     * @param source          The source object that generated the event.
     * @param partId          The identifier of the download part.
     * @param startRange      The start range of the download part.
     * @param endRange        The end range of the download part.
     * @param bytes           The total bytes downloaded in this part.
     * @param bytesSinceStart The bytes downloaded since the start of the download.
     * @param fileSizeAtStart The total file size at the start of the download.
     * @param cycleStart      The start time of the progress cycle.
     * @param cycleEnd        The end time of the progress cycle.
     */
    public PartProgressUpdateEvent(Object source, int partId, long startRange, long endRange, long bytes, long bytesSinceStart, long fileSizeAtStart,
            long cycleStart, long cycleEnd) {
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

    /**
     * Gets the identifier of the download part.
     *
     * @return The part identifier.
     */
    public int getPartId() {
        return mPartId;
    }

    /**
     * Gets the start range of the download part.
     *
     * @return The start range.
     */
    public long getStartRange() {
        return mStartRange;
    }

    /**
     * Gets the end range of the download part.
     *
     * @return The end range.
     */
    public long getEndRange() {
        return mEndRange;
    }

    /**
     * Gets the total bytes downloaded in this part.
     *
     * @return The total bytes.
     */
    public long getBytes() {
        return mBytes;
    }

    /**
     * Gets the bytes downloaded since the start of the download.
     *
     * @return The bytes since the start.
     */
    public long getBytesSinceStart() {
        return mBytesSinceStart;
    }

    /**
     * Gets the total file size at the start of the download.
     *
     * @return The file size at the start.
     */
    public long getFileSizeAtStart() {
        return mFileSizeAtStart;
    }

    /**
     * Gets the start time of the progress cycle.
     *
     * @return The cycle start time.
     */
    public long getCycleStart() {
        return mCycleStart;
    }

    /**
     * Gets the end time of the progress cycle.
     *
     * @return The cycle end time.
     */
    public long getCycleEnd() {
        return mCycleEnd;
    }

    /**
     * Returns a string representation of the PartProgressUpdateEvent.
     *
     * @return A string representation of the event.
     */
    @Override
    public String toString() {
        return "PartProgressUpdateEvent [mSource=" + mSource + ", mPartId=" + mPartId + ", mStartRange=" + mStartRange + ", mEndRange=" + mEndRange
                + ", mBytes=" + mBytes + ", mBytesSinceStart=" + mBytesSinceStart + ", mFileSizeAtStart=" + mFileSizeAtStart + ", mCycleStart=" + mCycleStart
                + ", mCycleEnd=" + mCycleEnd + "]";
    }
}
