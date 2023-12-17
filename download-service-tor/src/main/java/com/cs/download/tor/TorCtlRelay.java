package com.cs.download.tor;

public class TorCtlRelay {
  private String relayId;
  private String relayName;

  public TorCtlRelay(String relayInfo) {
      String[] parts = relayInfo.split("~");
      if (parts.length == 2) {
          this.relayId = parts[0];
          this.relayName = parts[1];
      } else {
          throw new IllegalArgumentException("Invalid relayInfo format: " + relayInfo);
      }
  }

  // Getters
  public String getRelayId() {
      return relayId;
  }

  public String getRelayName() {
      return relayName;
  }

  @Override
  public String toString() {
      return "TorRelay{" +
              "relayId='" + relayId + '\'' +
              ", relayName='" + relayName + '\'' +
              '}';
  }
}
