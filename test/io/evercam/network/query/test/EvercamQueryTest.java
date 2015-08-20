package io.evercam.network.query.test;

import static org.junit.Assert.*;
import io.evercam.EvercamException;
import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.query.EvercamQuery;

import org.junit.Test;

public class EvercamQueryTest 
{
	@Test
	public void testGetCameraVendorByMac() 
	{
		assertNull(EvercamQuery.getCameraVendorByMac("00:00:00"));
		assertNotNull(EvercamQuery.getCameraVendorByMac("8c:e7:48"));
	}
	
	@Test
	public void testGetThumbnailUrl() throws EvercamException
	{
		final String TEST_LOGO_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/logo.jpg";
		final String TEST_MODEL_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/ds-2cd7164-e/thumbnail.jpg";
		assertEquals(TEST_LOGO_URL, EvercamQuery.getThumbnailUrlFor("hikvision", "wrongModel"));
		assertEquals(TEST_MODEL_URL, EvercamQuery.getThumbnailUrlFor("hikvision", "ds-2cd7164-e"));
	}
	
	public void testFillCameraDefaults()
	{
		DiscoveredCamera testCamera = new DiscoveredCamera("192.168.0.88");
		testCamera.setVendor("dlink");
		testCamera.setModel("dcs-2121");
		EvercamQuery.fillDefaults(testCamera);
		assertEquals("play1.sdp", testCamera.getJpg());
		assertEquals("http://evercam-public-assets.s3.amazonaws.com/dlink/dcs-2121/thumbnail.jpg", testCamera.getModelThumbnail());
		assertEquals("http://evercam-public-assets.s3.amazonaws.com/dlink/logo.jpg", testCamera.getVendorThumbnail());
	}
}
