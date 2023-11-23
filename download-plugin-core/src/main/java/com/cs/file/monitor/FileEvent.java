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

import java.io.File;

import java.util.EventObject;

/**
 * An event that indicates a change in the state of a file.
 */
public class FileEvent extends EventObject {

  private static final long serialVersionUID = -626534786669993230L;

  /**
   * Constructs a FileEvent with the specified file.
   *
   * @param file The file associated with the event.
   */
  public FileEvent(File file) {
    super(file);
  }

  /**
   * Gets the file associated with this event.
   *
   * @return The file associated with this event.
   */
  public File getFile() {
    return (File) getSource();
  }
}