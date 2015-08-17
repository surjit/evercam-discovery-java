package io.evercam.network;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.query.EvercamQuery;

public abstract class EvercamQueryRunnable implements Runnable
{
	private boolean withDefaults = false;
	private DiscoveredCamera discoveredCamera;

	public EvercamQueryRunnable(DiscoveredCamera discoveredCamera)
	{
		this.discoveredCamera = discoveredCamera;
	}

	@Override
	public void run()
	{
		if (withDefaults)
		{
			EvercamDiscover.printLogMessage("Retrieving defaults for camera "
					+ discoveredCamera.getIP());
			discoveredCamera = EvercamQuery.fillDefaults(discoveredCamera);
		}

		onFinished();
	}

	public EvercamQueryRunnable withDefaults(boolean withDefaults)
	{
		this.withDefaults = withDefaults;
		return this;
	}

	public abstract void onFinished();
}
