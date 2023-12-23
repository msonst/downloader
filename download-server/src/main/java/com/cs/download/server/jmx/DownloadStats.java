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
package com.cs.download.server.jmx;

public class DownloadStats implements DownloadStatsMXBean {

  private int mThreadId;
  private long mStartRange;
  private long mEndRange;
  private long mBytes;
  private long mTransferred;
  private long mFileSzAtStart;
  private long mStart;
  private long mEnd;
  private double mCycleTime;
  private double mMbit;

  public void update(int threadId, long startRange, long endRange, long bytes, long transferred, long fileSzAtStart, long start, long end) {
    mThreadId = threadId;
    mStartRange = startRange;
    mEndRange = endRange;
    mBytes = bytes;
    mTransferred = transferred;
    mFileSzAtStart = fileSzAtStart;
    mStart = start;
    mEnd = end;
    mCycleTime = (double) (end - start);
    mMbit = (double) mBytes * (double) 8 / mCycleTime / (double) 1000 / (double) 1024 / (double) 1024;
  }

  public int getThreadId() {
    return mThreadId;
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

  public long getTransferred() {
    return mTransferred;
  }

  public long getFileSzAtStart() {
    return mFileSzAtStart;
  }

  public long getStart() {
    return mStart;
  }

  public long getEnd() {
    return mEnd;
  }

  public double getCycleTime() {
    return mCycleTime;
  }

  public double getMbit() {
    return mMbit;
  }

  public void add(int threadId, long startRange, long endRange, long bytes, long transferred, long fileSzAtStart, long start, long end) {
    mTransferred += bytes;
  }
}
