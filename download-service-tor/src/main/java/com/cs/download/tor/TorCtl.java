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

import com.cs.download.Retry;

@Component
public class TorCtl {
  private static final Logger LOGGER = LoggerFactory.getLogger(TorCtl.class);

  private static final String IP_RESOLVER = "https://api64.ipify.org?format=json";

  public String changeCircuit(String host, int torCtlPort, String torPass, Proxy verificationProxy) throws Exception {
    String ret = null;

    try (Socket s = new Socket(host, torCtlPort); OutputStream output = s.getOutputStream(); InputStream input = s.getInputStream()) {

      s.setSoTimeout(10000);

      PrintWriter out = new PrintWriter(s.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

      out.println(TorCtlCmd.AUTHENTICATE.parameter(torPass));
      String response = in.readLine();
      LOGGER.debug("Authentication " + TorCtlResponse.valueOf(response));

      List<TorCtlCircuit> circuits = Retry.times(10, () -> {
        Thread.sleep(1000);
        return getCircuits(out, in);
      }, v -> (v != null && !v.isEmpty()));

      LOGGER.debug("Requesting new circuit circuits [{}]", TorCtlCircuit.prettyStr(circuits));

      out.println(TorCtlCmd.SIGNAL_NEWNYM);
      LOGGER.debug("signal new circuit " + TorCtlResponse.valueOf(in.readLine()));

      List<TorCtlCircuit> newCircuits = Retry.times(30, () -> {
        Thread.sleep(3000);
        List<TorCtlCircuit> tmp = getCircuits(out, in);
        LOGGER.debug("IP after change reqest is [{}] was [{}]", TorCtlCircuit.prettyStr(tmp), TorCtlCircuit.prettyStr(circuits));
        return tmp;
      }, v -> (v != null && !circuits.equals(v)));

      
      ret =newCircuits.toString();
      
      out.println(TorCtlCmd.GETINFO_ADDRESS);
      LOGGER.debug("GETINFO_ADDRESS " + TorCtlResponse.valueOf(in.readLine()));

      out.println(TorCtlCmd.QUIT);
      s.close();
    } catch (Exception e) {
      LOGGER.error("Change IP failed [{}]", e.getMessage());
    }

    return ret;
  }

  private String getPublicIP(Proxy verificationProxy) {
    try {

      URL url = new URL(IP_RESOLVER);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection(verificationProxy);
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(3000);
      connection.setUseCaches(false);

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
        LOGGER.error("HTTP request failed with response code: " + connection.getResponseCode());
      }

      connection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Get IP failed [{}]", e.getMessage());
    }

    return "";
  }

  private String parsePublicIP(String jsonResponse) {
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

}
