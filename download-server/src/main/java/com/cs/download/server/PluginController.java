package com.cs.download.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/plugins")
public class PluginController {

//  private final DownloaderConfiguration mConfig;

//  public PluginController(DownloaderConfiguration config) {
//    mConfig = config;

//    PluginServiceRegistry mServiceRegistry = new PluginServiceRegistry();
//        mServiceRegistry.addPluginPath("./plugin");
//        mServiceRegistry.start();
//  }

//  @PostMapping("/upload")
//  @ResponseBody
//  public String upload(@RequestParam("file") MultipartFile file) throws IOException {
//    File targetFile = new File(mConfig.pluginDir() + File.separator + file.getOriginalFilename());
//    OutputStream fileOutputStream = new FileOutputStream(targetFile);
//
//    FileCopyUtils.copy(file.getInputStream(), fileOutputStream);
//
//    return "ok";
//  }
//
//  @GetMapping("/list")
//  @ResponseBody
//  public List<String> listPlugins() {
//    return Arrays.stream(Objects.requireNonNull(new File("plugins").listFiles())).map(File::getName).collect(Collectors.toList());
//  }

}
