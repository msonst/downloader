package com.cs.download.tor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.DownloadUtils;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshTunnel {
  private static final Logger LOGGER = LoggerFactory.getLogger(SshTunnel.class);

  private Session createSession(String sshHost, int sshPort, String sshUser, String sshPassword) throws JSchException {
    JSch sch = new JSch();
    com.jcraft.jsch.Session session = sch.getSession(sshUser, sshHost, sshPort);
    session.setUserInfo(new TorCtlUserInfo(sshPassword));
    session.connect();
    return session;
  }

  /**
   * equivalent to ssh -L [local_addr:]local_port:remote_addr:remote_port [user@]sshd_addr
   * 
   * On your machine, the SSH client will start listening on local_port
   * Any traffic to this port will be forwarded to the remote_private_addr:remote_port on the machine SSH-ed to.
   * 
   * @param sshUser
   * @param sshPassword
   * @param sshPort
   * @param remoteHost
   * @param remotePort
   * @throws JSchException
   * @throws IOException
   */
  public <T> T localPortForwarding(String sshUser, String sshPassword, int sshPort, String remoteHost, int remotePort, SessionHandler handler) {
    Session session = null;
    int localPort = 0;
    T ret = null;
    
    try {
      session = createSession(remoteHost, sshPort, sshUser, sshPassword);
      localPort = DownloadUtils.getFreePort();
      session.setPortForwardingL(localPort, remoteHost, remotePort);

      ret = handler.onConnected("LOCALHOST", localPort);

    } catch (Exception e) {
      handler.onError(e);
    } finally {
      try {
        session.delPortForwardingL(localPort);
      } catch (JSchException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      session.disconnect();
    }
    return ret;
  }
}
