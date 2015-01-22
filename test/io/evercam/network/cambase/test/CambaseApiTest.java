package io.evercam.network.cambase.test;

import static org.junit.Assert.assertEquals;
import io.evercam.network.cambase.CambaseAPI;
import io.evercam.network.cambase.CambaseException;
import org.junit.Test;

public class CambaseApiTest
{
	public String HIKVISION_LOGO_URL = "http://s3.amazonaws.com/cambase/images/files/000/001/918/small/c448d26102feff756f92f373201c4507.jpg?1415767726";
	final String THUMBNAIL_URL = "http://s3.amazonaws.com/cambase/images/files/000/008/282/small/37daf6241a6d3a848007245208386023.jpg?1416391442";
	
	@Test
	public void testGetSmallImageUrl()
	{
		String ORININAL = "http://s3.amazonaws.com/cambase/images/files/000/000/001/original/efc51157edefb56f3fcb4881236b9257.jpg?1401783706";
		String SMALL = "http://s3.amazonaws.com/cambase/images/files/000/000/001/small/efc51157edefb56f3fcb4881236b9257.jpg?1401783706";
		assertEquals(SMALL, CambaseAPI.getSmallImageUrl(ORININAL));
	}
	
	@Test
	public void testGetThumbnail() throws CambaseException
	{
		//test empty model
		String logoUrl = CambaseAPI.getThumbnailUrlFor("hikvision", "");
		assertEquals(HIKVISION_LOGO_URL,logoUrl);
		
		//test model that doesn't exixts
		assertEquals(HIKVISION_LOGO_URL, CambaseAPI.getThumbnailUrlFor("hikvision", "wrongmodel"));
		
		//test of vendor with model that exists
		assertEquals(THUMBNAIL_URL, CambaseAPI.getThumbnailUrlFor("hikvision", "ds-2cd7164-e"));
	}
}


