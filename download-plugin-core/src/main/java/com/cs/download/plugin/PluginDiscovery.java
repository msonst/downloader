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
package com.cs.download.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.plugin.api.PluginInfo;
import com.cs.file.monitor.FileAdapter;
import com.cs.file.monitor.FileEvent;
import com.cs.file.monitor.FileSystemMonitor;

/**
 * Utility class for discovering and monitoring plugins in a specified
 * directory.
 */
public class PluginDiscovery {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginDiscovery.class);
  private File mPluginDir;
  private FileSystemMonitor mMonitor;
  private List<PluginStateChangeListener> mListeners = new ArrayList<>();

  /**
   * Constructs a new PluginDiscovery instance with the specified directory name.
   *
   * @param dirName The name of the directory to monitor for plugins.
   */
  public PluginDiscovery(String dirName) {
    mPluginDir = new File(dirName);
    mMonitor = new FileSystemMonitor(mPluginDir);
  }

  /**
   * Starts the plugin discovery process and begins monitoring the specified
   * directory for changes.
   *
   * @return {@code true} if the discovery process started successfully;
   *         otherwise, {@code false}.
   */
  public boolean start() {
    mMonitor.addListener(new FileAdapter() {
      public void onCreated(FileEvent event) {
        File file = event.getFile();
        LOGGER.debug("NEW file in plugin folder [" + file.getName() + "]");

        List<PluginInfo> factories = new ArrayList<>();

        PluginLoader.provider(file).forEach(p -> {
          LOGGER.debug("File is a plugin. Loading file [" + file.getName() + "]");
          factories.add(p.getPluginInfo());
        });

        PluginEvent pluginEvent = new PluginEvent(factories.toArray(new PluginInfo[0]));
        mListeners.parallelStream().forEach(l -> l.onCreated(pluginEvent));
      }

      public void onModified(FileEvent event) {
        File file = event.getFile();
        LOGGER.debug("MODIFIED file in plugin folder [" + file.getName() + "]");
      }

      public void onDeleted(FileEvent event) {
        File file = event.getFile();
        LOGGER.debug("DELETED file in plugin folder [" + file.getName() + "]");
      }
    }).watch();

    return true;
  }

  /**
   * Stops the plugin discovery process.
   */
  public void stop() {
    // mMonitor.stop(); Uncomment this line if there is a method to stop the
    // monitor.
  }

  /**
   * Adds a listener for plugin state change events.
   *
   * @param listener The listener to add.
   */
  public void addListener(PluginStateChangeListener listener) {
    mListeners.add(listener);
  }
}
