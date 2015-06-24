package io.evercam.network.query.test;

import static org.junit.Assert.*;
import io.evercam.API;
import io.evercam.EvercamException;
import io.evercam.Vendor;
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
	public void testGetDefaults() throws EvercamException
	{
		Vendor hikvision = EvercamQuery.getCameraVendorByMac("8c:e7:48");
		assertEquals("admin", EvercamQuery.getDefaultUsernameByVendor(hikvision));
		assertEquals("12345",EvercamQuery.getDefaultPasswordByVendor(hikvision));
		assertEquals("Streaming/Channels/1/picture", EvercamQuery.getDefaultJpgUrlByVendor(hikvision));
		assertEquals("h264/ch1/main/av_stream", EvercamQuery.getDefaultH264UrlByVendor(hikvision));
	}
	
	@Test
	public void testGetThumbnailUrl() throws EvercamException
	{
		final String TEST_LOGO_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/logo.jpg";
		final String TEST_MODEL_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/ds-2cd7164-e/thumbnail.jpg";
		assertEquals(TEST_LOGO_URL, EvercamQuery.getThumbnailUrlFor("hikvision", "wrongModel"));
		assertEquals(TEST_MODEL_URL, EvercamQuery.getThumbnailUrlFor("hikvision", "ds-2cd7164-e"));
	}
}
