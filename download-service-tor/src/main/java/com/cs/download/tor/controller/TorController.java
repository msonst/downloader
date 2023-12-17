package com.cs.download.tor.controller;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs.download.Retry;
import com.cs.download.server.api.host.LifecycleState;
import com.cs.download.server.api.proxy.BandwidthProxyRequirement;
import com.cs.download.server.api.proxy.ProxyProviderService;
import com.cs.download.server.api.proxy.ProxyRequest;
import com.cs.download.server.api.proxy.ProxyRequirement;
import com.cs.download.server.api.proxy.ProxyResult;
import com.cs.download.tor.SessionHandler;
import com.cs.download.tor.SshTunnel;
import com.cs.download.tor.TorCtl;

@RestController
@RequestMapping("/tor")
public class TorController implements ProxyProviderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TorController.class);

  private LifecycleState mState;
  private @Autowired DockerTor mDockerTor;
  private @Autowired DockerConfig mDockerConfig;
  private @Autowired TorCtl mTorCtl;

  @GetMapping(value = "/next", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseBody
  @Override
  public ProxyResult next(@RequestBody ProxyRequest request) {
    int sshPort = mDockerConfig.getSshPort();
    String host = mDockerConfig.getHost();
    String sshUser = mDockerConfig.getSshUser();
    String sshPassword = mDockerConfig.getSshPassword();
    String torPass = mDockerConfig.getTorPassword();
    int dockerPort = mDockerConfig.getDockerPort();

    DockerTorResult res = initContainer(sshPort, host, sshUser, sshPassword, dockerPort, request.getId());

    int torCtlPort = res.getMapping().get("0.0.0.0").get(9051);
    int privoxyPort = res.getMapping().get("0.0.0.0").get(8118);

    LOGGER.debug("New Privoxy/Tor instance at {}:{} ctl={}", mDockerConfig.getHost(), privoxyPort, torCtlPort);

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }

    List<ProxyRequirement> requirements = request.getProxyRequirements();

    tuneContainer(sshPort, host, sshUser, sshPassword, torPass, torCtlPort, privoxyPort, requirements);

    return new ProxyResult(System.currentTimeMillis(), res.getContainerId(), Proxy.Type.HTTP.name(),host, privoxyPort);

  }

  private String tuneContainer(int sshPort, String host, String sshUser, String sshPassword, String torPass, int torCtlPort, int privoxyPort,
      List<ProxyRequirement> requirements) {
    return new SshTunnel().localPortForwarding(sshUser, sshPassword, sshPort, host, torCtlPort, new SessionHandler() {
      @Override
      public void onError(Exception exception) {
        LOGGER.error("Connect exception={}", exception.getMessage());
      }

      @Override
      public String onConnected(String localHost, int localPort) {
        String ret = null;
        try {
          Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, privoxyPort));

          ret = Retry.times(3, () -> mTorCtl.changeCircuit(localHost, localPort, torPass, proxy), v -> {
            LOGGER.info("Circuit requirement verification");
            boolean vResult = (null != v && requirements.stream().filter(r -> !r.isSatisfied(proxy)).collect(Collectors.toList()).isEmpty());
            LOGGER.info("Circuit requirement met={}", vResult);
            return vResult;
          });

        } catch (Exception exception) {
          LOGGER.error("Change Circuit exception={}", exception.getMessage());
        }
        return ret;
      }
    });
  }

  private DockerTorResult initContainer(int sshPort, String host, String sshUser, String sshPassword, int dockerPort, String id) {
    DockerTorResult ret = new SshTunnel().localPortForwarding(sshUser, sshPassword, sshPort, host, dockerPort, new SessionHandler() {
      @Override
      public void onError(Exception exception) {
        LOGGER.error("Connect exception={}", exception.getMessage());
      }

      @Override
      public DockerTorResult onConnected(String localHost, int localPort) {

        DockerTorResult ret = (id == null) ? mDockerTor.build(localHost, localPort, mDockerConfig.getFile(), mDockerConfig.getRegistryUrl())
            : new DockerTorResult(id);

        ret = (ret.getContainerId() == null) ? mDockerTor.create(ret) : ret;

        return mDockerTor.start(ret);
      }
    });
    return ret;
  }

  public static void main(String[] args) throws URISyntaxException {
    DockerConfig dockerConfig = new DockerConfig();
    dockerConfig.setFile("C:/Users/sonst00m/Documents/Privat/05-Hobby/Project/downloader/downloader/download-service-tor/config/tor/Dockerfile");
    dockerConfig.setHost("192.168.0.100");
    dockerConfig.setDockerPort(2375);
    dockerConfig.setRegistryUrl("om.local:5000");
    dockerConfig.setSshUser("kali");
    dockerConfig.setSshPassword("kali");
    dockerConfig.setSshPort(22);
    dockerConfig.setTorPassword("kali");
    dockerConfig.setTorPort(9050);

    ProxyRequest request = new ProxyRequest();
    request.addProxyRequirement(new BandwidthProxyRequirement(10));

    TorController uut = new TorController();
    uut.mDockerConfig = dockerConfig;
    uut.mDockerTor = new DockerTor();
    uut.mTorCtl = new TorCtl();

    uut.next(request);
  }
}
