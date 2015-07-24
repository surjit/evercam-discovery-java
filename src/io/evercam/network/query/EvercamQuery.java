package io.evercam.network.query;

import io.evercam.Auth;
import io.evercam.Defaults;
import io.evercam.EvercamException;
import io.evercam.Model;
import io.evercam.Vendor;
import io.evercam.network.Constants;
import io.evercam.network.EvercamDiscover;

import java.util.ArrayList;
import java.util.Locale;

public class EvercamQuery
{
	/**
	 * Query Evercam API to get camera vendor by MAC address.
	 * 
	 * @param macAddress
	 *            Full MAC address read from device.
	 */
	public static Vendor getCameraVendorByMac(String macAddress)
	{
		String submac = macAddress.substring(0, 8).toLowerCase(Locale.UK);

		try
		{
			ArrayList<Vendor> vendorList = Vendor.getByMac(submac);
			if (vendorList.size() > 0)
			{
				return Vendor.getByMac(submac).get(0);
			}
		}
		catch (EvercamException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	private static Defaults getDefaultsByVendor(Vendor vendor) throws EvercamException
	{
		return vendor.getDefaultModel().getDefaults();
	}

	/**
	 * @param vendor
	 *            Camera vendor returned from Evercam
	 * @return the default username of the specified vendor
	 * @throws EvercamException
	 *             if no default values associated with this vendor
	 */
	public static String getDefaultUsernameByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		Auth auth = defaults.getAuth(Auth.TYPE_BASIC);

		return auth == null ? "" : auth.getUsername();
	}

	/**
	 * @param vendor
	 *            Camera vendor returned from Evercam
	 * @return the default password of the specified vendor
	 * @throws EvercamException
	 *             if no default values associated with this vendor
	 */
	public static String getDefaultPasswordByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		Auth auth = defaults.getAuth(Auth.TYPE_BASIC);

		return auth == null ? "" : auth.getPassword();
	}

	/**
	 * @param vendor
	 *            Camera vendor returned from Evercam
	 * @return the default JPG snapshot URL of the specified vendor
	 * @throws EvercamException
	 *             if no default values associated with this vendor
	 */
	public static String getDefaultJpgUrlByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);

		return defaults.getJpgURL();
	}

	/**
	 * @param vendor
	 *            Camera vendor returned from Evercam
	 * @return the default h264 stream URL of the specified vendor
	 * @throws EvercamException
	 *             if no default values associated with this vendor
	 */
	public static String getDefaultH264UrlByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);

		return defaults.getH264URL();
	}

	/**
	 * Retrieve thumbnail URL by specifying camera vendor and model
	 * 
	 * @param vendorId
	 *            camera vendor ID for Evercam
	 * @param modelId
	 *            camera model ID for Evercam
	 * @return If no image associated with the specified model, return logo URL
	 *         for the specified vendor
	 * @throws EvercamException
	 *             if error occurred with Evercam
	 */
	public static String getThumbnailUrlFor(String vendorId, String modelId)
			throws EvercamException
	{
		String thumbnailUrl = "";
		if (!modelId.isEmpty())
		{
			modelId = modelId.toLowerCase(Locale.UK);
			if (!vendorId.isEmpty())
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
				EvercamDiscover.printLogMessage("Model " + modelId + " doesn't exist");
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
