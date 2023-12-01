package com.cs.download.plugin;

import java.util.Arrays;
import java.util.List;

import com.cs.download.api.plugin.service.ServiceFactory;
import com.cs.download.api.plugin.spi.Plugin;
import com.cs.download.api.plugin.spi.PluginInfo;
import com.cs.download.api.plugin.spi.PluginState;
import com.cs.download.plugin.impl.DefaultServiceFactoryImpl;

public class DefaultPluginProvider implements Plugin {

	private static final List<ServiceFactory> FACTORIES = Arrays.asList(new ServiceFactory[] { new DefaultServiceFactoryImpl() });
	private PluginState mState = PluginState.PLUGGED;


	@Override
	public PluginInfo getPluginInfo() {

		PluginInfo ret = new PluginInfo(mState, FACTORIES);

		return ret;
	}
}
