package com.cs.download.server.api.host;

public interface PluginService {
  
  LifecycleResult install();
  /**
   * Starts the service.
   *
   * @return The result of the start operation.
   */
  LifecycleResult start();

  /**
   * Stops the service.
   *
   * @return The result of the stop operation.
   */
  LifecycleResult stop();

  /**
   * Retrieves the status of the service.
   *
   * @return The result representing the status of the service.
   */
  LifecycleResult status();
}
