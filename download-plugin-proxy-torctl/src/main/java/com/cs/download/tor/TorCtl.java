package com.cs.download.tor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cs.download.DownloadUtils;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Component
public class TorCtl {
  private static final Logger LOGGER = LoggerFactory.getLogger(TorCtl.class);

  private static final String LOCALHOST = "127.0.0.1";
  private static final String IP_RESOLVER = "https://api64.ipify.org?format=json";

  protected Session createSession(String sshHost, int sshPort, String sshUser, String sshPassword) throws JSchException {
    JSch sch = new JSch();
    com.jcraft.jsch.Session session = sch.getSession(sshUser, sshHost, sshPort);
    session.setUserInfo(new TorCtlUserInfo(sshPassword));
    session.connect();
    return session;
  }

  public void changeIp(String sshUser, String sshPassword, int sshPort, String torHost, int torCtlPort,int torPort, String torPass) throws Exception {
    Session session = createSession(torHost, sshPort, sshUser, sshPassword);

    try {
      int localPort = DownloadUtils.getFreePort();
      session.setPortForwardingL(localPort, torHost, torCtlPort);

      try (Socket s = new Socket(LOCALHOST, localPort); OutputStream output = s.getOutputStream(); InputStream input = s.getInputStream()) {

        s.setSoTimeout(10000);

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Authentifizieren
        out.println(TorCtlCmd.AUTHENTICATE.parameter(torPass));
        String response = in.readLine();
        LOGGER.debug("Authentication " + TorCtlResponse.valueOf(response));

        String ip = "";
        do {
          ip = getPublicIP(torHost, torPort);
        } while (ip.isBlank());

        String newIp;
        do {
          // Befehl zum Wechseln der IP-Adresse senden
          out.println(TorCtlCmd.SIGNAL_NEWNYM);
          response = in.readLine();
          LOGGER.debug("signal new circuit " + TorCtlResponse.valueOf(response));

          Thread.sleep(10000);

          newIp = getPublicIP(torHost, torPort);

        } while (ip.equals(newIp));

        LOGGER.debug("Old IP {}, new IP {}", ip, newIp);

        // Verbindung schließen
        out.println(TorCtlCmd.QUIT);
        s.close();
      } finally {
        session.delPortForwardingL(localPort);
      }
    } finally {
      session.disconnect();
    }
  }

  private static String getPublicIP(String torHost, int torPort) {
    try {

      URL url = new URL(IP_RESOLVER);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(torHost, torPort)));
      connection.setConnectTimeout(500);

      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
          response.append(line);
        }

        reader.close();
        return parsePublicIP(response.toString());
      } else {
        System.out.println("HTTP request failed with response code: " + connection.getResponseCode());
      }

      connection.disconnect();
    } catch (IOException e) {
    }

    return "";
  }

  private static String parsePublicIP(String jsonResponse) {
    int startIndex = jsonResponse.indexOf("\"ip\":") + 6;
    int endIndex = jsonResponse.indexOf("\"", startIndex);

    return jsonResponse.substring(startIndex, endIndex);
  }

  private List<TorCtlCircuit> getCircuits(PrintWriter writer, BufferedReader reader) throws IOException {
    writer.println(TorCtlCmd.GETINFO_CIRCUIT_STATUS);
    String line;

    String cResponse = "";
    while ((line = reader.readLine()) != null && !line.equals(".")) {
      cResponse += line + "\n";
    }

    if (line.equals(".")) {
      reader.readLine();
      return TorCtlCircuitFactory.createTorCircuits(cResponse);
    }

    return null;
  }

  public static void main(String[] args) {
//
//    TorCtlServiceImpl uut = new TorCtlServiceImpl();
//    ProxyRequest request = new ProxyRequest("192.168.0.100", 9051, "", "pw");
//    request.addProxyRequirement(new BandwidthProxyRequirement(15.0));
//    uut.next(request);
  }
}
