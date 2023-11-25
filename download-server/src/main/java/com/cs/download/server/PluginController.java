package com.cs.download.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/plugins")
public class PluginController {

  private final DownloaderConfiguration mConfig;

  public PluginController(DownloaderConfiguration config) {
    mConfig = config;

    PluginServiceRegistry mServiceRegistry = new PluginServiceRegistry();
        mServiceRegistry.addPluginPath("./plugin");
        mServiceRegistry.start();
  }

  @PostMapping("/upload")
  @ResponseBody
  public String upload(@RequestParam("file") MultipartFile file) throws IOException {
    File targetFile = new File(mConfig.pluginDir() + File.separator + file.getOriginalFilename());
    OutputStream fileOutputStream = new FileOutputStream(targetFile);

    FileCopyUtils.copy(file.getInputStream(), fileOutputStream);

    return "ok";
  }

  @GetMapping("/list")
  @ResponseBody
  public List<String> listPlugins() {
    return Arrays.stream(Objects.requireNonNull(new File("plugins").listFiles())).map(File::getName).collect(Collectors.toList());
  }

}
