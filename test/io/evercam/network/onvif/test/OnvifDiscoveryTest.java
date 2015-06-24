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
		ArrayList<DiscoveredCamera> cameraList = new OnvifDiscovery(){

			@Override
			public void onActiveOnvifDevice(DiscoveredCamera discoveredCamera) {
				System.out.println("ONVIF camera discovered: " + discoveredCamera.toString());
			}}.probe();
		assertEquals(2, cameraList.size());
	}
}
