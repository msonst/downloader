package com.cs.download.plugin;

import java.util.Arrays;
import java.util.List;

import com.cs.download.plugin.api.PluginInfo;
import com.cs.download.plugin.api.PluginState;
import com.cs.download.plugin.api.ServiceFactory;
import com.cs.download.plugin.impl.HttpServerServiceFactoryImpl;
import com.cs.download.plugin.spi.ServiceFactoryProvider;

public class HttpServiceFactoryProvider implements ServiceFactoryProvider {

	private static final List<ServiceFactory> FACTORIES = Arrays.asList(new ServiceFactory[] { new HttpServerServiceFactoryImpl() });
	private PluginState mState = PluginState.PLUGGED;


	@Override
	public PluginInfo getPluginInfo() {

		PluginInfo ret = new PluginInfo(mState, FACTORIES);

		return ret;
	}
}
