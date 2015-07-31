package io.evercam.network;

import io.evercam.network.discovery.GatewayDevice;
import io.evercam.network.discovery.NatMapEntry;

import java.util.ArrayList;

public abstract class NatRunnable implements Runnable
{
	String routerIp;

	public NatRunnable(String routerIp)
	{
		this.routerIp = routerIp;
	}

	@Override
	public void run()
	{
		try
		{
			GatewayDevice gatewayDevice = new GatewayDevice(routerIp);
			onFinished(gatewayDevice.getNatTableArray());
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

	/**
	 * Callback for NAT discovery results
	 * 
	 * Check if mapEntries is null before invoking it
	 * 
	 * @param mapEntries
	 *            the discovered NAT table
	 */
	public abstract void onFinished(ArrayList<NatMapEntry> mapEntries);
}
