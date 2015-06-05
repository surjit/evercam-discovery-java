package io.evercam.network.onvif.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.onvif.OnvifDiscovery;

import org.junit.Test;

public class OnvifDiscoveryTest
{
	@Test
	public void testOnvifProbe()
	{
		ArrayList<DiscoveredCamera> cameraList = OnvifDiscovery.probe();
		assertEquals(2, cameraList.size());
	}
}
