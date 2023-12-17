package com.cs.download.tor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replies from Tor to the controller:
 * SyncReply:
 * ^(\d{3}-[^\r\n]*\r\n)+\d{3}[ \t][^\r\n]*\r\n$
 * This regex matches the structure of SyncReply, where multiple MidReplyLines may be present, followed by an EndReplyLine.
 *
 * AsyncReply:
 * ^(\d{3}-[^\r\n]*\r\n|\+\d{3}[ \t][^\r\n]*\r\n([\s\S]*?)\r\n\.)+$
 * This regex matches the structure of AsyncReply, where multiple MidReplyLines or DataReplyLines may be present, followed by an EndReplyLine.
 */
public final class TorCtlResponse {
  public static final TorCtlResponse UNKNOWN = new TorCtlResponse(-1);

  public static final TorCtlResponse OK = new TorCtlResponse(250);
  public static final TorCtlResponse OPERATION_UNNECESSARY = new TorCtlResponse(251);
  public static final TorCtlResponse RESOURCE_EXHAUSTED = new TorCtlResponse(451);
  public static final TorCtlResponse SYNTAX_ERROR_PROTOCOL = new TorCtlResponse(500);
  public static final TorCtlResponse UNRECOGNIZED_COMMAND = new TorCtlResponse(510);
  public static final TorCtlResponse UNIMPLEMENTED_COMMAND = new TorCtlResponse(511);
  public static final TorCtlResponse SYNTAX_ERROR_COMMAND_ARGUMENT = new TorCtlResponse(512);
  public static final TorCtlResponse UNRECOGNIZED_COMMAND_ARGUMENT = new TorCtlResponse(513);
  public static final TorCtlResponse AUTHENTICATION_REQUIRED = new TorCtlResponse(514);
  public static final TorCtlResponse BAD_AUTHENTICATION = new TorCtlResponse(515);
  public static final TorCtlResponse UNSPECIFIED_TOR_ERROR = new TorCtlResponse(550);
  public static final TorCtlResponse TorCtlResponseERNAL_ERROR = new TorCtlResponse(551);
  public static final TorCtlResponse UNRECOGNIZED_ENTITY = new TorCtlResponse(552);
  public static final TorCtlResponse INVALID_CONFIGURATION_VALUE = new TorCtlResponse(553);
  public static final TorCtlResponse INVALID_DESCRIPTOR = new TorCtlResponse(554);
  public static final TorCtlResponse UNMANAGED_ENTITY = new TorCtlResponse(555);
  public static final TorCtlResponse ASYNCHRONOUS_EVENT_NOTIFICATION = new TorCtlResponse(650);

  private static final String SYNC_CMD = "([0-9]+)\\s+([A-Za-z]+)";
  private static final String ASYNC_CMD = "([0-9]+)\\s+([A-Za-z]+)";
  private static final Pattern SYNC_PATTERN = Pattern.compile(SYNC_CMD);

  private int mResponse;

  public TorCtlResponse(int response) {
    mResponse = response;
  }

  public static TorCtlResponse valueOf(String string) {

    TorCtlResponse ret = UNKNOWN;
    if (null != string) {
      Matcher matcher = SYNC_PATTERN.matcher(string);

      if (matcher.find()) {
        try {
          ret = new TorCtlResponse(Integer.valueOf(matcher.group(1)));
        } catch (NumberFormatException e) {
          //
        }
      }
    }
    return ret;
  }

  @Override
  public String toString() {
    return "TorCtlResponse [mResponse=" + mResponse + "]";
  }
}