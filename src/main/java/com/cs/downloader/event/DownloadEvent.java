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

import java.util.EventObject;

/**
 * Base class for events related to the download process.
 */
public class DownloadEvent extends EventObject {

    private static final long serialVersionUID = -1558990924122036970L;

    /**
     * Constructs a new DownloadEvent with the specified source object.
     *
     * @param source The source object that generated the event.
     */
    public DownloadEvent(Object source) {
        super(source);
    }
}
