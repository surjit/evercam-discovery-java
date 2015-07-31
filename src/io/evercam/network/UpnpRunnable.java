package io.evercam.network;

import io.evercam.network.discovery.UpnpDevice;
import io.evercam.network.discovery.UpnpDiscovery;
import io.evercam.network.discovery.UpnpResult;

import java.util.ArrayList;

public abstract class UpnpRunnable implements Runnable
{
	
	@Override
	public void run()
	{
		try
		{
			UpnpDiscovery upnpDiscovery = new UpnpDiscovery(new UpnpResult()
			{
				@Override
				public void onUpnpDeviceFound(UpnpDevice upnpDevice)
				{
					onDeviceFound(upnpDevice);
				}
			});
			upnpDiscovery.discoverAll();
			onFinished(upnpDiscovery.getUpnpDevices());
		}
		catch (Exception e)
		{
			onFinished(null);
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
	}

	public abstract void onDeviceFound(UpnpDevice upnpDevice);

	public abstract void onFinished(ArrayList<UpnpDevice> upnpDeviceList);
}
