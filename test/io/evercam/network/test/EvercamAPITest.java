package io.evercam.network.test;

import static org.junit.Assert.assertEquals;
import io.evercam.EvercamException;
import io.evercam.network.EvercamAPI;

import org.junit.Test;

public class EvercamAPITest 
{
	@Test
	public void testGetThumbnailUrl() throws EvercamException
	{
		final String TEST_LOGO_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/logo.jpg";
		final String TEST_MODEL_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/ds-2cd7164-e/thumbnail.jpg";
		assertEquals(TEST_LOGO_URL, EvercamAPI.getThumbnailUrlFor("hikvision", "wrongModel"));
		assertEquals(TEST_MODEL_URL, EvercamAPI.getThumbnailUrlFor("hikvision", "ds-2cd7164-e"));
	}
}
