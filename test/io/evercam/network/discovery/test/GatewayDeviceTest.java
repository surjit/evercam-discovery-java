package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.GatewayDevice;
import io.evercam.network.discovery.NatMapEntry;

import org.junit.Test;

public class GatewayDeviceTest
{
	private final String TEST_ROUTER_IP = "172.16.0.1";
	@Test
	public void test() throws Exception
	{
		GatewayDevice gatewayDevice = new GatewayDevice(TEST_ROUTER_IP);
		assertFalse(gatewayDevice.isRouter());
		assertFalse(gatewayDevice.isUPnPAvaliable());
		
		ArrayList<NatMapEntry> mapEntries = gatewayDevice.getNatTableArray();
		if(mapEntries.size()!=0)
		{
			for (NatMapEntry mapEntry: mapEntries)
			{
				String natIP = mapEntry.getIpAddress();
				int natInternalPort = mapEntry.getInternalPort();
				int natExternalPort = mapEntry.getExternalPort();
				System.out.println(natIP + " " + natInternalPort + " " + natExternalPort);
			}
		}
	}
}
