package io.evercam.network.cambase;

import java.util.ArrayList;


public class CambaseAPI
{
	public static String getSmallImageUrl(String url)
	{
		String original = "/original/";
		String small = "/small/";
		return url.replace(original, small);
	}
	
	public static String getThumbnailUrlFor(String vendorId, String modelId) throws CambaseException
	{
		String thumbnailUrl = "";
		if (!modelId.isEmpty())
		{
			if (modelId.contains(vendorId))
			{
				modelId = modelId.replace(vendorId + " ", "");
			}
			CambaseModel model = new CambaseModel(modelId);
			ArrayList<String> thumnailUrls = model.getThumnailUrls();
			if (thumnailUrls.size() > 0)
			{
				thumbnailUrl = getSmallImageUrl(thumnailUrls.get(0));
			}
		}

		if (thumbnailUrl.isEmpty())
		{
			CambaseVendor cambaseVendor = new CambaseVendor(vendorId);
			thumbnailUrl = getSmallImageUrl(cambaseVendor.getLogoUrl());
		}
		return thumbnailUrl;
	}
}
