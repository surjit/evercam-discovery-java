package io.evercam.network.cambase;

import java.util.ArrayList;
import java.util.Locale;

/**
 * The helper class for Cambase API
 * Currently only be used to retrieve camera thumbnail
 */
public class CambaseAPI
{
	/**
	 * By default, Cambase returns the thumbnail in original size
	 * This method will replace it with the small size.
	 * 
	 * @param url the camera image URL that returned from Cambase
	 * @return the camera small size image URL
	 */
	public static String getSmallImageUrl(String url)
	{
		String original = "/original/";
		String small = "/small/";
		return url.replace(original, small);
	}
	
	/**
	 * Retrieve the small size thumbnail URL by specify camera vendor and model
	 * 
	 * @param vendorId camera vendor ID for Cambase
	 * @param modelId camera model ID for Cambase
	 * @return the small size thumbnail URL, if no image associated with the specified
	 * model, return logo URL for the specified vendor
	 * @throws CambaseException if error occurred with Cambase
	 */
	public static String getThumbnailUrlFor(String vendorId, String modelId) throws CambaseException
	{
		String thumbnailUrl = "";
		if (!modelId.isEmpty())
		{
			modelId = modelId.toLowerCase(Locale.UK);
			if(!vendorId.isEmpty())
			{
				vendorId = vendorId.toLowerCase(Locale.UK);
			}
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
