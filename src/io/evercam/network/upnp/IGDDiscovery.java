package io.evercam.network.upnp;

import java.io.IOException;
import java.util.ArrayList;

import net.sbbi.upnp.impls.InternetGatewayDevice;
import net.sbbi.upnp.messages.ActionResponse;
import net.sbbi.upnp.messages.UPNPResponseException;

/**
 * IGDDiscovery
 * 
 * Searching for Gateway device using UPnP and get NAT table.
 */

public class IGDDiscovery
{
	int discoveryTimeout = 5000; // 5 secs
	String routerIP;
	InternetGatewayDevice[] IGDs;
	public InternetGatewayDevice IGD = null;
	public int tableSize = 0;;
	public boolean isRouterIGD;

	public IGDDiscovery(String routerIP)
	{
		this.routerIP = routerIP;
		try
		{
			IGDs = InternetGatewayDevice.getDevices(discoveryTimeout);
			if (IGDs != null)
			{
				for (int i = 0; i < IGDs.length; i++)
				{
					String url = null;
					InternetGatewayDevice testIGD = IGDs[i];

					url = testIGD.getIGDRootDevice().getPresentationURL()
							.toString();
					if (url.contains(routerIP))
					{
						IGD = testIGD;
						tableSize = IGD.getNatTableSize();
						isRouterIGD = true;
					}
					else
					{
						isRouterIGD = false;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (UPNPResponseException e)
		{
			e.printStackTrace();
		}

		catch (NullPointerException e)
		{

		}
	}

	// is upnp enabled on router?
	public boolean isAvaliable()
	{
		if (IGD != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// get NAT mapped entity for specific IP
	public ArrayList<ActionResponse> getMatchedEntries(String ip)
	{
		ArrayList<ActionResponse> matchedEntrys = new ArrayList<ActionResponse>();
		for (int sizeIndex = 0; sizeIndex < tableSize; sizeIndex++)
		{
			try
			{
				ActionResponse mapEntry = IGD
						.getGenericPortMappingEntry(sizeIndex);
				String natIP = mapEntry
						.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_INTERNAL_CLIENT);
				if (natIP.equals(ip))
				{
					matchedEntrys.add(mapEntry);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (UPNPResponseException e)
			{
				e.printStackTrace();
			}
		}
		return matchedEntrys;
	}

}
