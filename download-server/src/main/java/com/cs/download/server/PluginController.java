package com.cs.download.server;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.download.api.plugin.PluginConfig;
import com.cs.download.plugin.PluginServiceRegistry;

@Controller
@RequestMapping("/plugin")
public class PluginController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);

  @Autowired
  private PluginConfig mConfig;

  @Autowired
  private PluginServiceRegistry mServiceRegistry;

  @GetMapping("/services")
  @ResponseBody
  public Set<String> services() {
    return mServiceRegistry.getServices();
  }

  @GetMapping("/files")
  @ResponseBody
  public List<String> listPlugins() {
    return Arrays.stream(Objects.requireNonNull(new File(mConfig.getMonitoringPath()).listFiles())).map(File::getName).collect(Collectors.toList());
  }
}
