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
package com.cs.downloader;

import java.util.HashMap;

public class DownloadStatusCode {
	// needs to be before instances
	private static final HashMap<Integer, DownloadStatusCode> VALUES = new HashMap<Integer, DownloadStatusCode>();

	// continue
	public static final DownloadStatusCode CONTINUE = new DownloadStatusCode(100);
	public static final DownloadStatusCode SWITCHING_PROTOCOLS = new DownloadStatusCode(101);
	public static final DownloadStatusCode PROCESSING = new DownloadStatusCode(102);
	// OK
	public static final DownloadStatusCode OK = new DownloadStatusCode(200);
	public static final DownloadStatusCode CREATED = new DownloadStatusCode(201);
	public static final DownloadStatusCode ACCEPTED = new DownloadStatusCode(202);
	public static final DownloadStatusCode NON_AUTHORITATIVE_INFORMATION = new DownloadStatusCode(203);
	public static final DownloadStatusCode NO_CONTENT = new DownloadStatusCode(204);
	public static final DownloadStatusCode RESET_CONTENT = new DownloadStatusCode(205);
	public static final DownloadStatusCode PARTIAL_CONTENT = new DownloadStatusCode(206);
	public static final DownloadStatusCode MULTI_STATUS = new DownloadStatusCode(207);
	// Redirection
	public static final DownloadStatusCode MULTIPLE_CHOICES = new DownloadStatusCode(300);
	public static final DownloadStatusCode MOVED_PERMANENTLY = new DownloadStatusCode(301);
	public static final DownloadStatusCode MOVED_TEMPORARILY = new DownloadStatusCode(302);
	public static final DownloadStatusCode SEE_OTHER = new DownloadStatusCode(303);
	public static final DownloadStatusCode NOT_MODIFIED = new DownloadStatusCode(304);
	public static final DownloadStatusCode USE_PROXY = new DownloadStatusCode(305);
	public static final DownloadStatusCode TEMPORARY_REDIRECT = new DownloadStatusCode(307);
	// Client Error
	public static final DownloadStatusCode BAD_REQUEST = new DownloadStatusCode(400);
	public static final DownloadStatusCode UNAUTHORIZED = new DownloadStatusCode(401);
	public static final DownloadStatusCode PAYMENT_REQUIRED = new DownloadStatusCode(402);
	public static final DownloadStatusCode FORBIDDEN = new DownloadStatusCode(403);
	public static final DownloadStatusCode NOT_FOUND = new DownloadStatusCode(404);
	public static final DownloadStatusCode METHOD_NOT_ALLOWED = new DownloadStatusCode(405);
	public static final DownloadStatusCode NOT_ACCEPTABLE = new DownloadStatusCode(406);
	public static final DownloadStatusCode PROXY_AUTHENTICATION_REQUIRED = new DownloadStatusCode(407);
	public static final DownloadStatusCode REQUEST_TIMEOUT = new DownloadStatusCode(408);
	public static final DownloadStatusCode CONFLICT = new DownloadStatusCode(409);
	public static final DownloadStatusCode GONE = new DownloadStatusCode(410);
	public static final DownloadStatusCode LENGTH_REQUIRED = new DownloadStatusCode(411);
	public static final DownloadStatusCode PRECONDITION_FAILED = new DownloadStatusCode(412);
	public static final DownloadStatusCode REQUEST_TOO_LONG = new DownloadStatusCode(413);
	public static final DownloadStatusCode REQUEST_URI_TOO_LONG = new DownloadStatusCode(414);
	public static final DownloadStatusCode UNSUPPORTED_MEDIA_TYPE = new DownloadStatusCode(415);
	public static final DownloadStatusCode REQUESTED_RANGE_NOT_SATISFIABLE = new DownloadStatusCode(416);
	public static final DownloadStatusCode EXPECTATION_FAILED = new DownloadStatusCode(417);
	public static final DownloadStatusCode REAUTHENTICATION_REQUIRED = new DownloadStatusCode(418);
	public static final DownloadStatusCode INSUFFICIENT_SPACE_ON_RESOURCE = new DownloadStatusCode(419);
	public static final DownloadStatusCode METHOD_FAILURE = new DownloadStatusCode(420);
	public static final DownloadStatusCode UNPROCESSABLE_ENTITY = new DownloadStatusCode(422);
	public static final DownloadStatusCode LOCKED = new DownloadStatusCode(423);
	public static final DownloadStatusCode FAILED_DEPENDENCY = new DownloadStatusCode(424);
	public static final DownloadStatusCode TOO_MANY_REQUESTS = new DownloadStatusCode(429);
	// Server Error
	public static final DownloadStatusCode INTERNAL_SERVER_ERROR = new DownloadStatusCode(500);
	public static final DownloadStatusCode NOT_IMPLEMENTED = new DownloadStatusCode(501);
	public static final DownloadStatusCode BAD_GATEWAY = new DownloadStatusCode(502);
	public static final DownloadStatusCode SERVICE_UNAVAILABLE = new DownloadStatusCode(503);
	public static final DownloadStatusCode GATEWAY_TIMEOUT = new DownloadStatusCode(504);
	public static final DownloadStatusCode HTTP_VERSION_NOT_SUPPORTED = new DownloadStatusCode(505);
	public static final DownloadStatusCode INSUFFICIENT_STORAGE = new DownloadStatusCode(507);

	public static final DownloadStatusCode COMPLETE = new DownloadStatusCode(0);
	public static final DownloadStatusCode ERROR = new DownloadStatusCode(-1);
	public static final DownloadStatusCode UNKNOWN = new DownloadStatusCode();

	public static final DownloadStatusCode INCOMPLETE = new DownloadStatusCode(-2);
	private int mResponseCode;

	private DownloadStatusCode(int responseCode) {
		mResponseCode = responseCode;
		VALUES.put(mResponseCode, this);
	}

	private DownloadStatusCode() {
	}

	public static DownloadStatusCode fromResponseCode(int responseCode) {
		DownloadStatusCode ret = VALUES.get(responseCode);
		if (null != ret)
			return ret;
		else
			return UNKNOWN;
	}

	public boolean isOK() {
		return (mResponseCode >= 200 && mResponseCode < 300) || mResponseCode == COMPLETE.getRespponseCode();
	}

	private int getRespponseCode() {
		return mResponseCode;
	}

	@Override
	public String toString() {
		return "DownloadStatus [mResponseCode=" + mResponseCode + "]";
	}

}
