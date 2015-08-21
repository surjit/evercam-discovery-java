package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.EvercamException;
import io.evercam.network.discovery.UpnpDevice;
import io.evercam.network.discovery.UpnpDiscovery;
import org.junit.Test;

public class UpnpDiscoveryTest
{
	@Test
	public void testUpnpDiscovery() throws EvercamException
	{
		UpnpDiscovery upnpDiscovery = new UpnpDiscovery(null);
		upnpDiscovery.discoverAll();

		ArrayList<UpnpDevice> deviceList = upnpDiscovery.getUpnpDevices();
		for (UpnpDevice device : deviceList)
		{
			System.out.println(device.toString());
		}
		assertTrue(deviceList.size() > 0);
	}
}
