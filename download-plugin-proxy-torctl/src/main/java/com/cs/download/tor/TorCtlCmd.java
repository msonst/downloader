package com.cs.download.tor;

import java.util.HashMap;

/**
 * Commands from controller to Tor
 */

public final class TorCtlCmd {
  private static final HashMap<String, TorCtlCmd> MAP = new HashMap<String, TorCtlCmd>();

  public static final TorCtlCmd QUIT = new TorCtlCmd("QUIT");
  public static final TorCtlCmd SETCONF = new TorCtlCmd("SETCONF");
  public static final TorCtlCmd RESETCONF = new TorCtlCmd("RESETCONF");
  public static final TorCtlCmd GETCONF = new TorCtlCmd("GETCONF");
  public static final TorCtlCmd SETEVENTS = new TorCtlCmd("SETEVENTS");
  public static final TorCtlCmd SAVECONF = new TorCtlCmd("SAVECONF");
  public static final TorCtlCmd SIGNAL = new TorCtlCmd("SIGNAL %s");
  public static final TorCtlCmd MAPADDRESS = new TorCtlCmd("MAPADDRESS");
  public static final TorCtlCmd GETINFO = new TorCtlCmd("GETINFO %s");
  public static final TorCtlCmd EXTENDCIRCUIT = new TorCtlCmd("EXTENDCIRCUIT");
  public static final TorCtlCmd SETCIRCUITPURPOSE = new TorCtlCmd("SETCIRCUITPURPOSE");
  public static final TorCtlCmd SETROUTERPURPOSE = new TorCtlCmd("SETROUTERPURPOSE");
  public static final TorCtlCmd ATTACHSTREAM = new TorCtlCmd("ATTACHSTREAM");
  public static final TorCtlCmd POSTDESCRIPTOR = new TorCtlCmd("POSTDESCRIPTOR");
  public static final TorCtlCmd REDIRECTSTREAM = new TorCtlCmd("REDIRECTSTREAM");
  public static final TorCtlCmd CLOSESTREAM = new TorCtlCmd("CLOSESTREAM");
  public static final TorCtlCmd CLOSECIRCUIT = new TorCtlCmd("CLOSECIRCUIT");
  public static final TorCtlCmd USEFEATURE = new TorCtlCmd("USEFEATURE");
  public static final TorCtlCmd RESOLVE = new TorCtlCmd("RESOLVE");
  public static final TorCtlCmd PROTOCOLINFO = new TorCtlCmd("PROTOCOLINFO");
  public static final TorCtlCmd LOADCONF = new TorCtlCmd("LOADCONF");
  public static final TorCtlCmd TAKEOWNERSHIP = new TorCtlCmd("TAKEOWNERSHIP");
  public static final TorCtlCmd AUTHCHALLENGE = new TorCtlCmd("AUTHCHALLENGE");
  public static final TorCtlCmd DROPGUARDS = new TorCtlCmd("DROPGUARDS");
  public static final TorCtlCmd HSFETCH = new TorCtlCmd("HSFETCH");
  public static final TorCtlCmd ADD_ONION = new TorCtlCmd("ADD_ONION");
  public static final TorCtlCmd DEL_ONION = new TorCtlCmd("DEL_ONION");
  public static final TorCtlCmd HSPOST = new TorCtlCmd("HSPOST");
  public static final TorCtlCmd ONION_CLIENT_AUTH_ADD = new TorCtlCmd("ONION_CLIENT_AUTH_ADD");
  public static final TorCtlCmd ONION_CLIENT_AUTH_REMOVE = new TorCtlCmd("ONION_CLIENT_AUTH_REMOVE");
  public static final TorCtlCmd ONION_CLIENT_AUTH_VIEW = new TorCtlCmd("ONION_CLIENT_AUTH_VIEW");
  public static final TorCtlCmd DROPOWNERSHIP = new TorCtlCmd("DROPOWNERSHIP");
  public static final TorCtlCmd DROPTIMEOUTS = new TorCtlCmd("DROPTIMEOUTS");
  public static final TorCtlCmd AUTHENTICATE = new TorCtlCmd("AUTHENTICATE \"%s\"");

  public static final TorCtlCmd SIGNAL_RELOAD = SIGNAL.parameter("RELOAD");
  public static final TorCtlCmd SIGNAL_SHUTDOWN = SIGNAL.parameter("SHUTDOWN");
  public static final TorCtlCmd SIGNAL_DUMP = SIGNAL.parameter("DUMP");
  public static final TorCtlCmd SIGNAL_DEBUG = SIGNAL.parameter("DEBUG");
  public static final TorCtlCmd SIGNAL_HALT = SIGNAL.parameter("HALT");
  public static final TorCtlCmd SIGNAL_CLEARDNSCACHE = SIGNAL.parameter("CLEARDNSCACHE");
  public static final TorCtlCmd SIGNAL_NEWNYM = SIGNAL.parameter("NEWNYM");
  public static final TorCtlCmd SIGNAL_HEARTBEAT = SIGNAL.parameter("HEARTBEAT");
  public static final TorCtlCmd SIGNAL_DORMANT = SIGNAL.parameter("DORMANT");
  public static final TorCtlCmd SIGNAL_ACTIVE = SIGNAL.parameter("ACTIVE");
  public static final TorCtlCmd GETINFO_VERSION = GETINFO.parameter("version");
  public static final TorCtlCmd GETINFO_CIRCUIT_STATUS = GETINFO.parameter("circuit-status");
  public static final TorCtlCmd GETINFO_CONFIG_FILE = GETINFO.parameter("config-file");
  public static final TorCtlCmd GETINFO_CONFIG_DEFAULTS_FILE = GETINFO.parameter("config-defaults-file");
  public static final TorCtlCmd GETINFO_CONFIG_TEXT = GETINFO.parameter("config-text");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_DEFAULT = GETINFO.parameter("exit-policy/default");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_REJECT_PRIVATE_DEFAULT = GETINFO.parameter("exit-policy/reject-private/default");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_REJECT_PRIVATE_RELAY = GETINFO.parameter("exit-policy/reject-private/relay");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_IPV4 = GETINFO.parameter("exit-policy/ipv4");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_IPV6 = GETINFO.parameter("exit-policy/ipv6");
  public static final TorCtlCmd GETINFO_EXIT_POLICY_FULL = GETINFO.parameter("exit-policy/full");

  private String mCmd;
  private String mParameter;

  private TorCtlCmd(String cmd) {
    mCmd = cmd;
    MAP.put(mCmd, this);
  }

  public TorCtlCmd(String cmd, String parameter) {
    this(cmd);
    mParameter = parameter;
  }

  public TorCtlCmd parameter(String parameter) {
    mParameter = parameter;
    return new TorCtlCmd(mCmd, mParameter);
  }

  @Override
  public String toString() {
    return (null != mParameter) ? mCmd.formatted(mParameter) : mCmd.replace("%s", "");
  }

}