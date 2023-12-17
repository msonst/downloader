package com.cs.download.tor.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

@Component
public class DockerTor {
  private static final Logger LOGGER = LoggerFactory.getLogger(DockerTor.class);

  private DockerClient mDockerClient;

  public DockerTor() {

    //  mDockerConfig = dockerConfig;

  }

  public DockerTorResult build(String dockerHost, int dockerPort, String dockerFile, String registryUrl) {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://" + dockerHost + ":" + dockerPort)//"tcp://192.168.0.200:2376")
        .withDockerTlsVerify(false)/*.withDockerCertPath(mDockerConfig.getPath())*/.withRegistryUrl(registryUrl).build();

    DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost())//.sslConfig(config.getSSLConfig())
        .maxConnections(100).connectionTimeout(Duration.ofSeconds(30)).responseTimeout(Duration.ofSeconds(45)).build();

    mDockerClient = DockerClientImpl.getInstance(config, httpClient);

    String imageId = mDockerClient.buildImageCmd().withDockerfile(new File(dockerFile)).withPull(true).withNoCache(true).withTag("tor1:dyn")
        .exec(new BuildImageResultCallback()).awaitImageId();

    LOGGER.debug("Build done imageId=" + imageId);

    return new DockerTorResult(imageId);
  }

  public DockerTorResult create(DockerTorResult ret) {
    
    //ExposedPort tcp9050 = ExposedPort.tcp(9050);
    ExposedPort tcp9051 = ExposedPort.tcp(9051);
    ExposedPort tcp8118 = ExposedPort.tcp(8118);

    Ports portBindings = new Ports();
  //portBindings.bind(tcp9050, Ports.Binding.empty());
  portBindings.bind(tcp9051, Ports.Binding.empty());
  portBindings.bind(tcp8118, Ports.Binding.empty());

    CreateContainerResponse container = mDockerClient.createContainerCmd("tor1:dyn")
        //.withExposedPorts(tcp9050)
        .withExposedPorts(tcp9051).withExposedPorts(tcp8118)
        .withHostConfig(new HostConfig()
            .withPortBindings(portBindings)).exec();
    LOGGER.debug("Create done id=" + container.getId());
    
    ret.setContainerId(container.getId());
    
    return ret;
  }

  @SuppressWarnings("deprecation")
  public DockerTorResult start(DockerTorResult ret) {

    Map<String, Map<Integer, Integer>> mapping = new HashMap<String, Map<Integer, Integer>>();
    mDockerClient.startContainerCmd(ret.getContainerId()).exec();
    LOGGER.debug("Start done id=" + ret.getContainerId());

    InspectContainerResponse inspectResponse = mDockerClient.inspectContainerCmd(ret.getContainerId()).exec();
    
    Ports portMapping = inspectResponse.getNetworkSettings().getPorts();
    LOGGER.debug("Inspect portMapping=" + portMapping.toPrimitive());
    
    Map<ExposedPort, Binding[]> bindings = portMapping.getBindings();
    
    bindings.entrySet().forEach(e -> {
      int dockerPort = e.getKey().getPort();
      Binding[] bind = e.getValue();

      for (int i = 0; (null != bind) && (i < bind.length); i++) {
        Binding binding = bind[i];
        String hostIp = binding.getHostIp();
        try {
          int hostPort = Integer.parseInt(binding.getHostPortSpec());
          Map<Integer, Integer> map = mapping.get(hostIp);
          if (null == map) {
            map = new HashMap<>();
            mapping.put(hostIp, map);
          }
          map.put(dockerPort, hostPort);
        } catch (Exception ex) {
          LOGGER.warn(ex.getMessage());
        }

      }
    });
    
    ret.setMapping(mapping);
    
    return ret;
  }

  public static void main(String[] args) throws URISyntaxException {
    DockerConfig dockerConfig = new DockerConfig();
    dockerConfig.setFile("C:/Users/sonst00m/Documents/Privat/05-Hobby/Project/downloader/downloader/download-service-tor/config/tor/Dockerfile");
    dockerConfig.setHost("192.168.0.100");
    dockerConfig.setDockerPort(2375);
    dockerConfig.setRegistryUrl("om.local:5000");

    DockerTor uut = new DockerTor();
    DockerTorResult result = uut.build(dockerConfig.getHost(), dockerConfig.getDockerPort(), dockerConfig.getFile(), dockerConfig.getRegistryUrl());
    result = uut.create(result);
    uut.start(result);
  }
  //
  //  public static void main(String[] args) throws InterruptedException {
  //
  //

  //
  //    System.out.println(dockerClient.listContainersCmd().exec());
  //
  //    List<Container> containers = dockerClient.listContainersCmd().withShowSize(true).withShowAll(true)
  //        .withStatusFilter(Arrays.asList(new String[] { "exited" })).exec();
  //
  //
  //   ;
  //    dockerClient.stopContainerCmd(container.getId()).exec();
  //    dockerClient.killContainerCmd(container.getId()).exec();
  //
  //    InspectContainerResponse container1 = dockerClient.inspectContainerCmd(container.getId()).exec();
  //
  //    String snapshotId = dockerClient.commitCmd("3464bb547f88").withAuthor("Baeldung <info@baeldung.com>").withEnv("SNAPSHOT_YEAR=2018")
  //        .withMessage("add git support").withCmd("git", "version").withRepository("alpine").withTag("3.6.git").exec();
  //
  //    List<Image> images = dockerClient.listImagesCmd().exec();
  //

  //
  //    InspectImageResponse image = dockerClient.inspectImageCmd("161714540c41").exec();
  //
  //    dockerClient.pushImageCmd("baeldung/alpine").withTag("git").exec(new PushImageResultCallback()).awaitCompletion(90, TimeUnit.SECONDS);
  //
  //    dockerClient.pullImageCmd("baeldung/alpine").withTag("git").exec(new PullImageResultCallback()).awaitCompletion(30, TimeUnit.SECONDS);
  //
  //    dockerClient.removeImageCmd("beaccc8687ae").exec();
  //
  //    ListVolumesResponse volumesResponse = dockerClient.listVolumesCmd().exec();
  //    List<InspectVolumeResponse> volumes = volumesResponse.getVolumes();
  //
  //    CreateVolumeResponse namedVolume = dockerClient.createVolumeCmd().withName("myNamedVolume").exec();
  //
  //    List<Network> networks = dockerClient.listNetworksCmd().exec();
  //
  //    //    CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd()
  //    //        .withName("baeldung")
  //    //        .withIpam(new Ipam()
  //    //          .withConfig(new Config()
  //    //          .withSubnet("172.36.0.0/16")
  //    //          .withIpRange("172.36.5.0/24")))
  //    //        .withDriver("bridge").exec();
  //  }

}
