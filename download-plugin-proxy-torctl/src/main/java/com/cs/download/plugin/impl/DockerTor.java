package com.cs.download.plugin.impl;

import java.io.File;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

import com.cs.download.ResourceExtractor;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

@Component
public class DockerTor {
  private @Autowired DockerConfig mDockerConfig;
  private DockerClient mDockerClient;
  private CreateContainerResponse mContainer;

  public DockerTor() {

    //  mDockerConfig = dockerConfig;

  }

  public void build() {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(mDockerConfig.getHost())//"tcp://192.168.0.200:2376")
        .withDockerTlsVerify(true).withDockerCertPath(mDockerConfig.getPath()).withRegistryUrl(mDockerConfig.getRegistryUrl()).build();

    DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).sslConfig(config.getSSLConfig())
        .maxConnections(100).connectionTimeout(Duration.ofSeconds(30)).responseTimeout(Duration.ofSeconds(45)).build();

    mDockerClient = DockerClientImpl.getInstance(config, httpClient);

    new File(mDockerConfig.getWorkDir()).mkdirs();

    ResourceExtractor.extract("tor/", mDockerConfig.getWorkDir());
    String imageId = mDockerClient.buildImageCmd().withDockerfile(new File(mDockerConfig.getWorkDir() + "/tor/Dockerfile")).withPull(true)
        .withNoCache(true).withTag("tor:dyn").exec(new BuildImageResultCallback()).awaitImageId();
  }

  public void create() {
    mContainer = mDockerClient.createContainerCmd("tor:dyn").withCmd("--bind_ip_all").withName("tor").withPortBindings(PortBinding.parse("9050:9050"))
        .exec();
  }

  public void start() {
    mDockerClient.startContainerCmd(mContainer.getId()).exec();
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
