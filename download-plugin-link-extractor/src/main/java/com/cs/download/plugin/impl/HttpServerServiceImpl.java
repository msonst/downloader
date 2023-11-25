package com.cs.download.plugin.impl;

import java.security.Provider;
import java.security.Provider.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.plugin.api.PluginInfo;
import com.cs.download.plugin.api.ServiceConfiguration;
import com.cs.download.plugin.api.ServiceResult;

public class HttpServerServiceImpl extends Service {


  private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerServiceImpl.class);

	private static final String NAME = "Http Server";

	private static final PluginInfo OK = null;

	private static final ServiceConfiguration CONFIG = new ServiceConfiguration();

	private Future<ServiceResult> mFuture;

	static {
		CONFIG.put("net.host", "localhost");
		CONFIG.put("net.port", 8080);
	}
	public HttpServerServiceImpl(Provider provider, String type, String algorithm, String className, List<String> aliases,
      Map<String, String> attributes) {
    super(provider, type, algorithm, className, aliases, attributes);
  }


	public static String getName() {
		return NAME;
	}
}