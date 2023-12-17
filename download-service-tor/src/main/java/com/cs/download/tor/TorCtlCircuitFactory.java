package com.cs.download.tor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorCtlCircuitFactory {
  private static final Pattern CIRCUIT_PATTERN = Pattern.compile(
      "(\\d+) (BUILT|EXTENDED) ((?:\\$[\\w~]+~[^,]+)+(?:,\\$[\\w~]+~[^,]+)*) BUILD_FLAGS=([^\\s]+) PURPOSE=([^\\s]+)(?: SOCKS_USERNAME=\"([^\"]+)\")? TIME_CREATED=([^\\s]+)");

  public static List<TorCtlCircuit> createTorCircuits(String input) {
    List<TorCtlCircuit> torCircuits = new ArrayList<>();

    String[] circuitEntries = input.split("\n");

    for (String circuitEntry : circuitEntries) {
      TorCtlCircuit torCircuit = createTorCircuitFromEntry(circuitEntry);
      if (torCircuit != null) {
        torCircuits.add(torCircuit);
      }
    }

    return torCircuits;
  }

  private static TorCtlCircuit createTorCircuitFromEntry(String entry) {
    Matcher matcher = CIRCUIT_PATTERN.matcher(entry);

    if (matcher.find()) {
      int circuitId = Integer.parseInt(matcher.group(1));
      List<TorCtlRelay> relayList = parseRelays(matcher.group(3));
      String buildFlags = matcher.group(4);
      String purpose = matcher.group(5);
      String socksUsername = matcher.group(6);
      String timeCreated = matcher.group(7);
      boolean isExtended = "EXTENDED".equals(matcher.group(2));

      return new TorCtlCircuit(circuitId, relayList, buildFlags, purpose, socksUsername, timeCreated, isExtended);
    } else {
//      System.out.println("Failed to parse entry: " + entry);
      return null;
    }
  }

  private static List<TorCtlRelay> parseRelays(String relayString) {
    String[] relayArray = relayString.split(",");
    List<TorCtlRelay> relayList = new ArrayList<>();

    for (String relayInfo : relayArray) {
      TorCtlRelay torRelay = new TorCtlRelay(relayInfo.trim());
      relayList.add(torRelay);
    }

    return relayList;
  }

}
