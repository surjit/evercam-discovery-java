package io.evercam.network;

import io.evercam.EvercamException;
import io.evercam.Model;
import io.evercam.Vendor;

import java.util.Locale;

public class EvercamAPI 
{
	/**
	 * Retrieve thumbnail URL by specifying camera vendor and model
	 * 
	 * @param vendorId camera vendor ID for Evercam
	 * @param modelId camera model ID for Evercam
	 * @return If no image associated with the specified model, return 
	 *         logo URL for the specified vendor
	 * @throws EvercamException if error occurred with Evercam
	 */
	public static String getThumbnailUrlFor(String vendorId, String modelId) throws EvercamException
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
			try
			{
				Model model = Model.getById(modelId);
				thumbnailUrl = model.getThumbnailUrl();
			}
			catch (EvercamException e)
			{
				System.out.println("Model " + modelId + " doesn't exist");
			}
		}

		if (thumbnailUrl.isEmpty())
		{
			Vendor vendor = Vendor.getById(vendorId);
			thumbnailUrl = vendor.getLogoUrl();
		}
		return thumbnailUrl;
	}
}
