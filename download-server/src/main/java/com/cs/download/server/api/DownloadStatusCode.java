/*
 *     Copyright 2023 Michael Sonst @ https://www.corporate-startup.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cs.download.server.api;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents the status codes for HTTP download responses.
 */
public class DownloadStatusCode {
  // needs to be before instances
  private static final HashMap<Integer, DownloadStatusCode> VALUES = new HashMap<Integer, DownloadStatusCode>();

  // continue
  /**
   * Continue (100): The client should continue with its request.
   */
  public static final DownloadStatusCode CONTINUE = new DownloadStatusCode(100);

  /**
   * Switching Protocols (101): The server is switching protocols as a result of
   * the client's request.
   */
  public static final DownloadStatusCode SWITCHING_PROTOCOLS = new DownloadStatusCode(101);

  /**
   * Processing (102): The server is processing the client's request, but no
   * response is available yet.
   */
  public static final DownloadStatusCode PROCESSING = new DownloadStatusCode(102);

  // OK
  /**
   * OK (200): The request was successful.
   */
  public static final DownloadStatusCode OK = new DownloadStatusCode(200);

  /**
   * Created (201): The request was successful, and a new resource was created.
   */
  public static final DownloadStatusCode CREATED = new DownloadStatusCode(201);

  /**
   * Accepted (202): The request has been accepted but not yet processed.
   */
  public static final DownloadStatusCode ACCEPTED = new DownloadStatusCode(202);

  /**
   * Non-Authoritative Information (203): The information returned is not
   * authoritative.
   */
  public static final DownloadStatusCode NON_AUTHORITATIVE_INFORMATION = new DownloadStatusCode(203);

  /**
   * No Content (204): The server successfully processed the request but there is
   * no content to send.
   */
  public static final DownloadStatusCode NO_CONTENT = new DownloadStatusCode(204);

  /**
   * Reset Content (205): The server successfully processed the request and reset
   * the current content.
   */
  public static final DownloadStatusCode RESET_CONTENT = new DownloadStatusCode(205);

  /**
   * Partial Content (206): The server is delivering only part of the resource due
   * to a range header sent by the client.
   */
  public static final DownloadStatusCode PARTIAL_CONTENT = new DownloadStatusCode(206);

  /**
   * Multi-Status (207): The message body that follows is an XML message and can
   * contain a number of separate response codes.
   */
  public static final DownloadStatusCode MULTI_STATUS = new DownloadStatusCode(207);

  // Redirection
  /**
   * Multiple Choices (300): The requested resource corresponds to any one of a
   * set of representations, each with its own specific location.
   */
  public static final DownloadStatusCode MULTIPLE_CHOICES = new DownloadStatusCode(300);

  /**
   * Moved Permanently (301): The requested resource has been assigned a new
   * permanent URI.
   */
  public static final DownloadStatusCode MOVED_PERMANENTLY = new DownloadStatusCode(301);

  /**
   * Moved Temporarily (302): The requested resource resides temporarily under a
   * different URI.
   */
  public static final DownloadStatusCode MOVED_TEMPORARILY = new DownloadStatusCode(302);

  /**
   * See Other (303): The response to the request can be found under a different
   * URI.
   */
  public static final DownloadStatusCode SEE_OTHER = new DownloadStatusCode(303);

  /**
   * Not Modified (304): The resource has not been modified since the version
   * specified in the request headers.
   */
  public static final DownloadStatusCode NOT_MODIFIED = new DownloadStatusCode(304);

  /**
   * Use Proxy (305): The requested resource must be accessed through the proxy
   * given by the Location field.
   */
  public static final DownloadStatusCode USE_PROXY = new DownloadStatusCode(305);

  /**
   * Temporary Redirect (307): The requested resource resides temporarily under a
   * different URI.
   */
  public static final DownloadStatusCode TEMPORARY_REDIRECT = new DownloadStatusCode(307);

  // Client Error
  /**
   * Bad Request (400): The server could not understand the request.
   */
  public static final DownloadStatusCode BAD_REQUEST = new DownloadStatusCode(400);

  /**
   * Unauthorized (401): The request requires user authentication.
   */
  public static final DownloadStatusCode UNAUTHORIZED = new DownloadStatusCode(401);

  /**
   * Payment Required (402): This response code is reserved for future use.
   */
  public static final DownloadStatusCode PAYMENT_REQUIRED = new DownloadStatusCode(402);

  /**
   * Forbidden (403): The server understood the request but refuses to authorize
   * it.
   */
  public static final DownloadStatusCode FORBIDDEN = new DownloadStatusCode(403);

  /**
   * Not Found (404): The server did not find the requested resource.
   */
  public static final DownloadStatusCode NOT_FOUND = new DownloadStatusCode(404);

  /**
   * Method Not Allowed (405): The method specified in the request is not allowed
   * for the resource identified by the request URI.
   */
  public static final DownloadStatusCode METHOD_NOT_ALLOWED = new DownloadStatusCode(405);

  /**
   * Not Acceptable (406): The server cannot produce a response matching the list
   * of acceptable values.
   */
  public static final DownloadStatusCode NOT_ACCEPTABLE = new DownloadStatusCode(406);

  /**
   * Proxy Authentication Required (407): The client must first authenticate
   * itself with the proxy.
   */
  public static final DownloadStatusCode PROXY_AUTHENTICATION_REQUIRED = new DownloadStatusCode(407);

  /**
   * Request Timeout (408): The server timed out waiting for the request.
   */
  public static final DownloadStatusCode REQUEST_TIMEOUT = new DownloadStatusCode(408);

  /**
   * Conflict (409): The request could not be completed because of a conflict.
   */
  public static final DownloadStatusCode CONFLICT = new DownloadStatusCode(409);

  /**
   * Gone (410): The requested resource is no longer available.
   */
  public static final DownloadStatusCode GONE = new DownloadStatusCode(410);

  /**
   * Length Required (411): The server requires a content-length in the request.
   */
  public static final DownloadStatusCode LENGTH_REQUIRED = new DownloadStatusCode(411);

  /**
   * Precondition Failed (412): The precondition given in the request headers
   * fields evaluated to false by the server.
   */
  public static final DownloadStatusCode PRECONDITION_FAILED = new DownloadStatusCode(412);

  /**
   * Request Too Long (413): The server will not process the request because it is
   * too large.
   */
  public static final DownloadStatusCode REQUEST_TOO_LONG = new DownloadStatusCode(413);

  /**
   * Request URI Too Long (414): The server will not process the request because
   * the URI is too long.
   */
  public static final DownloadStatusCode REQUEST_URI_TOO_LONG = new DownloadStatusCode(414);

  /**
   * Unsupported Media Type (415): The server will not accept the request unless
   * the client presents a valid media type.
   */
  public static final DownloadStatusCode UNSUPPORTED_MEDIA_TYPE = new DownloadStatusCode(415);

  /**
   * Requested Range Not Satisfiable (416): The client has asked for a portion of
   * the file but the server cannot supply that portion.
   */
  public static final DownloadStatusCode REQUESTED_RANGE_NOT_SATISFIABLE = new DownloadStatusCode(416);

  /**
   * Expectation Failed (417): The server cannot meet the requirements of the
   * Expect request-header field.
   */
  public static final DownloadStatusCode EXPECTATION_FAILED = new DownloadStatusCode(417);

  /**
   * Reauthentication Required (418): Reserved for future use.
   */
  public static final DownloadStatusCode REAUTHENTICATION_REQUIRED = new DownloadStatusCode(418);

  /**
   * Insufficient Space on Resource (419): The server would return this response
   * if the request method is known by the server but has been disabled and cannot
   * be used.
   */
  public static final DownloadStatusCode INSUFFICIENT_SPACE_ON_RESOURCE = new DownloadStatusCode(419);

  /**
   * Method Failure (420): Reserved for future use.
   */
  public static final DownloadStatusCode METHOD_FAILURE = new DownloadStatusCode(420);

  /**
   * Unprocessable Entity (422): The request was well-formed but was unable to be
   * followed due to semantic errors.
   */
  public static final DownloadStatusCode UNPROCESSABLE_ENTITY = new DownloadStatusCode(422);

  /**
   * Locked (423): The resource that is being accessed is locked.
   */
  public static final DownloadStatusCode LOCKED = new DownloadStatusCode(423);

  /**
   * Failed Dependency (424): The request failed because it depended on another
   * request and that request failed.
   */
  public static final DownloadStatusCode FAILED_DEPENDENCY = new DownloadStatusCode(424);

  /**
   * Too Many Requests (429): The user has sent too many requests in a given
   * amount of time.
   */
  public static final DownloadStatusCode TOO_MANY_REQUESTS = new DownloadStatusCode(429);

  // Server Error
  /**
   * Internal Server Error (500): An unexpected condition was encountered by the
   * server and no more specific message is suitable.
   */
  public static final DownloadStatusCode INTERNAL_SERVER_ERROR = new DownloadStatusCode(500);

  /**
   * Not Implemented (501): The server either does not recognize the request
   * method, or it lacks the ability to fulfill the request.
   */
  public static final DownloadStatusCode NOT_IMPLEMENTED = new DownloadStatusCode(501);

  /**
   * Bad Gateway (502): The server was acting as a gateway or proxy and received
   * an invalid response from the upstream server.
   */
  public static final DownloadStatusCode BAD_GATEWAY = new DownloadStatusCode(502);

  /**
   * Service Unavailable (503): The server is currently unavailable (overloaded or
   * down).
   */
  public static final DownloadStatusCode SERVICE_UNAVAILABLE = new DownloadStatusCode(503);

  /**
   * Gateway Timeout (504): The server was acting as a gateway or proxy and did
   * not receive a timely response from the upstream server.
   */
  public static final DownloadStatusCode GATEWAY_TIMEOUT = new DownloadStatusCode(504);

  /**
   * HTTP Version Not Supported (505): The server does not support the HTTP
   * protocol version that was used in the request.
   */
  public static final DownloadStatusCode HTTP_VERSION_NOT_SUPPORTED = new DownloadStatusCode(505);

  /**
   * Insufficient Storage (507): The server is unable to store the representation
   * needed to complete the request.
   */
  public static final DownloadStatusCode INSUFFICIENT_STORAGE = new DownloadStatusCode(507);

  /**
   * Special Code - Complete (0): Indicates successful completion.
   */
  public static final DownloadStatusCode COMPLETE = new DownloadStatusCode(0);

  /**
   * Special Code - Error (-1): Indicates an error occurred.
   */
  public static final DownloadStatusCode ERROR = new DownloadStatusCode(-1);

  /**
   * Special Code - Unknown: Represents an unknown status code.
   */
  public static final DownloadStatusCode UNKNOWN = new DownloadStatusCode();

  /**
   * Special Code - Incomplete (-2): Indicates the operation was incomplete.
   */
  public static final DownloadStatusCode INCOMPLETE = new DownloadStatusCode(-2);

  public static final DownloadStatusCode INITIALIZED = new DownloadStatusCode(-3);
  public static final DownloadStatusCode DOWNLOADING = new DownloadStatusCode(-4);

  public static final DownloadStatusCode MERGING = new DownloadStatusCode(-5);

  private int mResponseCode;

  private String mMessage;

  /**
   * Constructs a new DownloadStatusCode instance with the specified HTTP response
   * code.
   *
   * @param responseCode The HTTP response code.
   */
  private DownloadStatusCode(int responseCode) {
    mResponseCode = responseCode;
    VALUES.put(mResponseCode, this);
  }

  /**
   * Constructs a new DownloadStatusCode instance.
   */
  private DownloadStatusCode() {
  }

  /**
   * Gets the DownloadStatusCode instance associated with the given HTTP response
   * code.
   *
   * @param responseCode The HTTP response code.
   * @return The DownloadStatusCode instance.
   */
  @JsonIgnore
  public static DownloadStatusCode fromResponseCode(int responseCode) {
    DownloadStatusCode ret = VALUES.get(responseCode);
    if (null != ret)
      return ret;
    else
      return UNKNOWN;
  }

  /**
   * Checks
   * 
   * /** Checks if the HTTP response code indicates a successful operation (2xx
   * range).
   *
   * @return True if the response code is in the 2xx range, false otherwise.
   */
  @JsonIgnore
  public boolean isOK() {
    return ((mResponseCode >= 200 && mResponseCode < 300) || mResponseCode == COMPLETE.getResponseCode()) && ("" == mMessage || null == mMessage);
  }

  /**
   * Gets the HTTP response code associated with this DownloadStatusCode instance.
   *
   * @return The HTTP response code.
   */
  public int getResponseCode() {
    return mResponseCode;
  }

  public void setResponseCode(int responseCode) {
     mResponseCode = responseCode;
  }
  
  /**
   * Returns a string representation of the DownloadStatusCode.
   *
   * @return A string representation of the DownloadStatusCode.
   */
  @Override
  public String toString() {
    return "DownloadStatus [mResponseCode=" + mResponseCode + "]";
  }

  public DownloadStatusCode setMessage(String message) {
    mMessage = message;

    return this;
  }

  public String getMessage() {
    return (null == mMessage) ? "" : mMessage;
  }
  @JsonIgnore
  public boolean isDone() {
    // TODO Auto-generated method stub
    return false;
  }
}
