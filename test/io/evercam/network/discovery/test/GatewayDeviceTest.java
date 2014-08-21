package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.GatewayDevice;
import io.evercam.network.discovery.UpnpDevice;
import io.evercam.network.discovery.UpnpDiscovery;

import net.sbbi.upnp.messages.ActionResponse;

import org.junit.Test;

public class GatewayDeviceTest
{
	private final String TEST_ROUTER_IP = "192.168.1.1";
	@Test
	public void test() throws Exception
	{
		GatewayDevice gatewayDevice = new GatewayDevice(TEST_ROUTER_IP);
		assertTrue(gatewayDevice.isRouter());
		assertTrue(gatewayDevice.isUPnPAvaliable());
		
		ArrayList<ActionResponse> mapEntries = gatewayDevice.getNatTableArray();
		if(mapEntries.size()!=0)
		{
			for (ActionResponse mapEntry: mapEntries)
			{
				String natIP = mapEntry
						.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_INTERNAL_CLIENT);
				int natInternalPort = Integer.parseInt(mapEntry
						.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_INTERNAL_PORT));
				int natExternalPort = Integer.parseInt(mapEntry
						.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_EXTERNAL_PORT));
				System.out.println(natIP + " " + natInternalPort + " " + natExternalPort);
			}
		}
	}
}
