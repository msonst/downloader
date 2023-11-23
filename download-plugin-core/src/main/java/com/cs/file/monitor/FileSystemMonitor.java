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
package com.cs.file.monitor;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Monitors a file system for changes and notifies registered listeners.
 */
public class FileSystemMonitor implements Runnable {

  protected List<FileListener> mListeners = new ArrayList<>();
  protected final List<WatchService> mWatchServices = new ArrayList<>();
  private final File mFolder;

  /**
   * Creates a new FileSystemMonitor for the specified folder.
   *
   * @param folder The folder to monitor for changes.
   */
  public FileSystemMonitor(final File folder) {
    mFolder = folder;
  }

  /**
   * Starts watching the specified folder for file system events.
   */
  public void watch() {
    if (mFolder.exists()) {
      Thread thread = new Thread(this);
      thread.setDaemon(true);
      thread.start();
    }
  }

  @Override
  public void run() {
    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
      Path path = Paths.get(mFolder.getAbsolutePath());
      path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
      mWatchServices.add(watchService);

      boolean poll = true;

      while (poll) {
        poll = pollEvents(watchService);
      }
    } catch (IOException | InterruptedException | ClosedWatchServiceException e) {
      Thread.currentThread().interrupt();
    }
  }

  protected boolean pollEvents(WatchService watchService) throws InterruptedException {
    WatchKey key = watchService.take();
    Path path = (Path) key.watchable();

    for (WatchEvent<?> event : key.pollEvents()) {
      notifyListeners(event.kind(), path.resolve((Path) event.context()).toFile());
    }

    return key.reset();
  }

  protected void notifyListeners(WatchEvent.Kind<?> kind, File file) {
    FileEvent event = new FileEvent(file);

    if (kind == ENTRY_CREATE) {
      for (FileListener listener : mListeners) {
        listener.onCreated(event);
      }

      if (file.isDirectory()) {
        new FileSystemMonitor(file).setListeners(mListeners).watch();
      }
    } else if (kind == ENTRY_MODIFY) {
      for (FileListener listener : mListeners) {
        listener.onModified(event);
      }
    } else if (kind == ENTRY_DELETE) {
      for (FileListener listener : mListeners) {
        listener.onDeleted(event);
      }
    }
  }

  /**
   * Adds a file listener to be notified of file system events.
   *
   * @param listener The file listener to add.
   * @return The current FileSystemMonitor instance for method chaining.
   */
  public FileSystemMonitor addListener(FileListener listener) {
    mListeners.add(listener);

    Arrays.stream(Optional.of(mFolder.listFiles()).orElse(new File[] {})).map(f -> new FileEvent(f)).forEach(e -> listener.onCreated(e));

    return this;
  }

  /**
   * Removes a file listener.
   *
   * @param listener The file listener to remove.
   * @return The current FileSystemMonitor instance for method chaining.
   */
  public FileSystemMonitor removeListener(FileListener listener) {
    mListeners.remove(listener);
    return this;
  }

  // Additional methods and getters (commented out) can be uncommented based on your needs.

  //    public List<FileListener> getListeners() {
  //        return mListeners;
  //    }

  /**
   * Sets the list of file listeners to be notified of file system events.
   *
   * @param listeners The list of file listeners to set.
   * @return The current FileSystemMonitor instance for method chaining.
   */
  private FileSystemMonitor setListeners(List<FileListener> listeners) {
    this.mListeners = listeners;
    return this;
  }
  //    public static List<WatchService> getWatchServices() {
  //        return Collections.unmodifiableList(mWatchServices);
  //    }
}
