package io.evercam.network.cambase.test;

import static org.junit.Assert.assertEquals;
import io.evercam.network.cambase.CambaseException;
import io.evercam.network.cambase.CambaseModel;
import io.evercam.network.cambase.CambaseVendor;

import java.util.ArrayList;

import org.junit.Test;

public class CambaseVendotTest
{
	@Test
	public void testGetManufacturerLogo() throws CambaseException
	{
		final String TEST_LOGO_URL = "http://s3.amazonaws.com/cambaseio/images/files/000/001/095/original/30fc846844deb1a32c4a2dfdfb43ec48.jpg?1413364450";
		CambaseVendor cambaseVendor = new CambaseVendor("hikvision");
		assertEquals(TEST_LOGO_URL , cambaseVendor.getLogoUrl());
	}
	
	@Test
	public void testGetModelImageUrl() throws CambaseException
	{
		String TSET_MODEL = "ds-2cd7164-e";
		CambaseModel model = new CambaseModel(TSET_MODEL);
		ArrayList<String> urlList = model.getThumnailUrls();
		assertEquals(3,urlList.size());
	}
}
