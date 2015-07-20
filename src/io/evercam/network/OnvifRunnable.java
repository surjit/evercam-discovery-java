package io.evercam.network;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.onvif.OnvifDiscovery;

public abstract class OnvifRunnable implements Runnable
{
	@Override
	public void run()
	{
		new OnvifDiscovery()
		{
			@Override
			public void onActiveOnvifDevice(DiscoveredCamera discoveredCamera)
			{
				onDeviceFound(discoveredCamera);
			}
		}.probe();

		onFinished();
	}

	public abstract void onDeviceFound(DiscoveredCamera discoveredCamera);

	public abstract void onFinished();
}
