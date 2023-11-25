package com.cs.download.plugin.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.plugin.api.Service;
import com.cs.download.plugin.api.ServiceFactory;
import com.cs.download.plugin.api.ServiceInfo;
import com.cs.download.plugin.api.ServiceState;

public class HttpServerServiceFactoryImpl implements ServiceFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerServiceFactoryImpl.class);

	private static final List<ServiceInfo> SERVICES = new ArrayList<ServiceInfo>();

	static {
		SERVICES.add(new ServiceInfo(ServiceState.STOPPED, HttpServerServiceImpl.getName()));
	}

	@Override
	public Service getService(ServiceInfo serviceInfo) {


		return null;
	}

	@Override
	public List<ServiceInfo> getServiceInfo() {
		return SERVICES;
	}


}
