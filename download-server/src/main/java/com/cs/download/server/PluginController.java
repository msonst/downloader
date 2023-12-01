package com.cs.download.server;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.download.plugin.PluginDiscovery;
import com.cs.download.plugin.PluginServiceRegistry;

@Controller
@RequestMapping("/plugin")
public class PluginController {

  private final DownloaderConfiguration mConfig;

  @Autowired
  private PluginServiceRegistry mServiceRegistry;

  public PluginController(DownloaderConfiguration config) {
    mConfig = config;
  }

  @GetMapping("/services")
  @ResponseBody
  public Set<String> services() {
    return mServiceRegistry.getServices();
  }

  @GetMapping("/files")
  @ResponseBody
  public List<String> listPlugins() {
    //mPluginDiscoveries
    return null;//Arrays.stream(Objects.requireNonNull(new File(mConfig.pluginDir()).listFiles())).map(File::getName).collect(Collectors.toList());
  }
}
