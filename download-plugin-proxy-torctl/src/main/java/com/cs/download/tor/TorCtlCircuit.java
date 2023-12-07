package com.cs.download.tor;

import java.util.List;

public class TorCtlCircuit {
  private int circuitId;
  private List<TorCtlRelay> relayList; // List of TorRelay objects
  private String buildFlags;
  private String purpose;
  private String socksUsername; // New field
  private String timeCreated;
  private boolean isExtended; // New field

  public TorCtlCircuit(int circuitId, List<TorCtlRelay> relayList, String buildFlags, String purpose, String socksUsername, String timeCreated,
      boolean isExtended) {
    this.circuitId = circuitId;
    this.relayList = relayList;
    this.buildFlags = buildFlags;
    this.purpose = purpose;
    this.socksUsername = socksUsername;
    this.timeCreated = timeCreated;
    this.isExtended = isExtended;
  }

  // Getters
  public int getCircuitId() {
    return circuitId;
  }

  public List<TorCtlRelay> getRelayList() {
    return relayList;
  }

  public String getBuildFlags() {
    return buildFlags;
  }

  public String getPurpose() {
    return purpose;
  }

  public String getSocksUsername() {
    return socksUsername;
  }

  public String getTimeCreated() {
    return timeCreated;
  }

  public boolean isExtended() {
    return isExtended;
  }

  @Override
  public String toString() {
    return "TorCircuit{" + "circuitId=" + circuitId + ", relayList=" + relayList + ", buildFlags='" + buildFlags + '\'' + ", purpose='" + purpose
        + '\'' + ", socksUsername='" + socksUsername + '\'' + ", timeCreated='" + timeCreated + '\'' + ", isExtended=" + isExtended + '}';
  }
}
