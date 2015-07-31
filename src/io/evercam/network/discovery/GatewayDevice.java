package io.evercam.network.discovery;

import io.evercam.network.Constants;

import java.io.IOException;
import java.util.ArrayList;

import net.sbbi.upnp.impls.InternetGatewayDevice;
import net.sbbi.upnp.messages.ActionResponse;
import net.sbbi.upnp.messages.UPNPResponseException;

/**
 * GatewayDevice
 * 
 * Searching for gateway device using UPnP and retrieve NAT table from router.
 */

public class GatewayDevice
{
	private int discoveryTimeout = 5000; // 5 secs
	private InternetGatewayDevice[] gatewayDevices;
	private boolean isRouter = false;;
	private InternetGatewayDevice IGD = null;
	private int tableSize = 0;;

	public GatewayDevice(String routerIP) throws Exception
	{
		try
		{
			gatewayDevices = InternetGatewayDevice.getDevices(discoveryTimeout);
			if (gatewayDevices != null)
			{
				for (int i = 0; i < gatewayDevices.length; i++)
				{
					String url = null;
					InternetGatewayDevice testIGD = gatewayDevices[i];

					url = testIGD.getIGDRootDevice().getPresentationURL().toString();
					if (url.contains(routerIP))
					{
						IGD = testIGD;
						if(IGD != null)
						{
							tableSize = IGD.getNatTableSize();
						}
						isRouter = true;
					}
					else
					{
						isRouter = false;
					}
				}
			}
		}
		catch (IOException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		catch (UPNPResponseException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
	}

	public InternetGatewayDevice getIGD()
	{
		return IGD;
	}

	public int getTableSize()
	{
		return tableSize;
	}

	// Is UPnP enabled on router?
	public boolean isUPnPAvaliable()
	{
		return IGD != null;
	}

	// get NAT mapped entity for specific IP
	public ArrayList<NatMapEntry> getMatchedEntries(String ip)
	{
		ArrayList<NatMapEntry> matchedEntrys = new ArrayList<NatMapEntry>();
		for (int sizeIndex = 0; sizeIndex < tableSize; sizeIndex++)
		{
			try
			{
				ActionResponse actionResponse = IGD.getGenericPortMappingEntry(sizeIndex);
				NatMapEntry mapEntry = new NatMapEntry(actionResponse);
				String natIP = mapEntry.getIpAddress();
				if (natIP.equals(ip))
				{
					matchedEntrys.add(mapEntry);
				}
			}
			catch (IOException e)
			{
				if(Constants.ENABLE_LOGGING)
				{
					e.printStackTrace();
				}
			}
			catch (UPNPResponseException e)
			{
				if(Constants.ENABLE_LOGGING)
				{
					e.printStackTrace();
				}
			}
		}
		return matchedEntrys;
	}

	public boolean isRouter()
	{
		return isRouter;
	}

	/**
	 * Return UPnP table as list Return an empty list if the device is not
	 * router or not UPnP enabled.
	 */
	public ArrayList<NatMapEntry> getNatTableArray()
	{
		ArrayList<NatMapEntry> mapEntries = new ArrayList<NatMapEntry>();
		if (isRouter() && isUPnPAvaliable())
		{
			try
			{
				for (int sizeIndex = 0; sizeIndex < tableSize; sizeIndex++)
				{
					ActionResponse actionResponse = IGD.getGenericPortMappingEntry(sizeIndex);
					mapEntries.add(new NatMapEntry(actionResponse));
				}
			}
			catch (IOException e)
			{
				if(Constants.ENABLE_LOGGING)
				{
					e.printStackTrace();
				}
			}
			catch (UPNPResponseException e)
			{
				if(Constants.ENABLE_LOGGING)
				{
					e.printStackTrace();
				}
			}
		}
		return mapEntries;
	}
}
