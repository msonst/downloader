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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.cs.download.api.plugin.spi.Plugin;

/**
 * Utility class for loading and managing plugins using the Service Provider Interface (SPI).
 */
public class PluginLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoader.class);

  private static final String DEFAULT_PROVIDER = "com.baeldung.rate.spi.YahooFinanceBdaPluginProvider";

  /**
   * Creates a URLClassLoader for loading plugins from the specified directory.
   *
   * @param dir The directory containing plugin JAR files.
   * @return A URLClassLoader configured with the URLs of the plugin JAR files.
   */
  private static URLClassLoader createPluginClassLoader(File dir) {
    final URL[] urls = Arrays.stream(Optional.of(dir.listFiles()).orElse(new File[] {})).sorted().map(File::toURI).map(PluginLoader::toUrl)
        .toArray(URL[]::new);

    return new URLClassLoader(urls, PluginLoader.class.getClassLoader());
  }

  /**
   * Converts a URI to a URL.
   *
   * @param uri The URI to convert.
   * @return The corresponding URL.
   */
  private static URL toUrl(final URI uri) {
    try {
      return uri.toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves all ServiceFactoryProvider instances available in the plugins directory.
   *
   * @return A list of ServiceFactoryProvider instances.
   */
  public static List<Plugin> providers() {
    List<Plugin> services = new ArrayList<>();
    List<Plugin> loader = SpringFactoriesLoader.loadFactories(Plugin.class, createPluginClassLoader(new File("./plugins")));
//    ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class, createPluginClassLoader(new File("./plugins")));
    loader.forEach(services::add);
    return services;
  }

  /**
   * Retrieves the default ServiceFactoryProvider.
   *
   * @return The default ServiceFactoryProvider.
   */
  public static Plugin provider() {
    return provider(DEFAULT_PROVIDER);
  }

  /**
   * Retrieves a ServiceFactoryProvider by name.
   *
   * @param providerName The name of the desired provider.
   * @return The ServiceFactoryProvider with the specified name.
   * @throws ProviderNotFoundException If the specified provider is not found.
   */
  public static Plugin provider(String providerName) {
    ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class, createPluginClassLoader(new File("./plugins")));
    Iterator<Plugin> it = loader.iterator();
    while (it.hasNext()) {
      Plugin provider = it.next();
      if (providerName.equals(provider.getClass().getName())) {
        return provider;
      }
    }
    throw new ProviderNotFoundException("Provider " + providerName + " not found");
  }

  /**
   * Retrieves all ServiceFactoryProvider instances from a specific file.
   *
   * @param file The JAR file containing the ServiceFactoryProvider implementations.
   * @return A list of ServiceFactoryProvider instances.
   */
  public static List<Plugin> provider(File file) {
    List<Plugin> services = new ArrayList<>();
    URL[] url = new URL[] { toUrl(file.toURI()) };
    URLClassLoader classLoader = new URLClassLoader(url, PluginLoader.class.getClassLoader());
    ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class, classLoader);
    loader.forEach(services::add);
    return services;
  }
}
