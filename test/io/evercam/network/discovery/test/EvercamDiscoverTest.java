package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.EvercamDiscover;
import io.evercam.network.discovery.DiscoveredCamera;

import org.junit.Test;

public class EvercamDiscoverTest
{
	private final String IP1 = "192.168.1.1";
	private final String IP2 = "192.168.1.2";
	
	@Test
	public void testReviewCameraList()
	{
		DiscoveredCamera camera1 = new DiscoveredCamera(IP1);
		camera1.setHttp(80);
		DiscoveredCamera camera2 = new DiscoveredCamera(IP1);
		camera2.setMAC("mac2");
		DiscoveredCamera camera3 = new DiscoveredCamera(IP2);
		DiscoveredCamera camera4 = new DiscoveredCamera(IP2);
		camera4.setMAC("mac4");
		ArrayList<DiscoveredCamera> cameraList = new ArrayList<>();
		cameraList.add(camera1);
		cameraList.add(camera2);
		cameraList.add(camera3);
		cameraList.add(camera4);
		
		EvercamDiscover.mergeDuplicateCameraFromList(cameraList);
		assertEquals(2, cameraList.size());
		for(DiscoveredCamera camera : cameraList)
		{
			System.out.println(camera.toJsonObject().toString());
		}
	}
}
