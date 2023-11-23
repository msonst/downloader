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
package com.cs.download.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class for the file downloader.
 *
 * This class is annotated with {@code @Component}, indicating that it is a Spring bean
 * that can be automatically discovered and registered in the Spring application context.
 * It is used to configure the behavior of the {@code DownloadController} and {@code DownloadManager}.
 *
 * Constructs a new {@code DownloaderConfiguration} record.
 *
 * @param proxyHost           The proxy host to be used for downloads.
 * @param proxyPort           The proxy port to be used for downloads.
 * @param outpath             The path where downloaded files will be saved.
 * @param parallelDownloads   The maximum number of parallel downloads allowed.
 */
@Component
public record DownloaderConfiguration(@Value("${proxy.host}") String proxyHost, @Value("${proxy.port}") int proxyPort,
    @Value("${app.outpath}") String outpath, @Value("${app.parallel_downloads}") int parallelDownloads) {
}
