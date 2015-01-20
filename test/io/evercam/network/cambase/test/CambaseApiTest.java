package io.evercam.network.cambase.test;

import static org.junit.Assert.assertEquals;
import io.evercam.network.cambase.CambaseAPI;
import io.evercam.network.cambase.CambaseException;
import org.junit.Test;

public class CambaseApiTest
{
	public String HIKVISION_LOGO_URL = "http://s3.amazonaws.com/cambase/images/files/000/001/918/small/c448d26102feff756f92f373201c4507.jpg?1415767726";
	
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
		String logoUrl = CambaseAPI.getThumbnailUrlFor("hikvision", "");
		assertEquals(HIKVISION_LOGO_URL,logoUrl);
		
		//TODO: test case of vendor with model
	}
}


