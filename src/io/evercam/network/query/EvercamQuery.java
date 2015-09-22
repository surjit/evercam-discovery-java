package io.evercam.network.query;

import io.evercam.Auth;
import io.evercam.Defaults;
import io.evercam.EvercamException;
import io.evercam.Model;
import io.evercam.Vendor;
import io.evercam.network.Constants;
import io.evercam.network.EvercamDiscover;
import io.evercam.network.discovery.DiscoveredCamera;

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
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	 * @deprecated it's not recommend to use the vendor logo as thumbnail if model </br>
	 * 			   does not exist.
	 */
	@Deprecated
	public static String getThumbnailUrlFor(String vendorId, String modelId)
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

			thumbnailUrl = getModelThumbnailUrl(modelId);
		}

		if (thumbnailUrl.isEmpty())
		{
			thumbnailUrl = getVendorThumbnailUrl(vendorId);
		}
		return thumbnailUrl;
	}
	
	/**
	 * Fill all defaults (default username & password, JPG & H264 path and </br>
	 * model & vendor thumbnail URLs) for the specified camera by sending API requests
	 * 
	 * @param discoveredCamera the discovered camera object, identification must </br>
	 * 		  has been finished
	 *	
	 * @return the camera object with defaults info
	 */
	public static DiscoveredCamera fillDefaults(DiscoveredCamera discoveredCamera)
	{
		try
		{
			Model cameraModel = null;
			Defaults defaults = null;
			if(discoveredCamera.hasModel())
			{
				try
				{
					cameraModel =  Model.getById(discoveredCamera.getModel().toLowerCase(Locale.UK));
				}
				catch (Exception e)
				{
					EvercamDiscover.printLogMessage("Model " + discoveredCamera.getModel() + " doesn't exist");
				}
			}
			
			if(cameraModel != null)
			{
				discoveredCamera.setModelThumbnail(cameraModel.getThumbnailUrl());
				defaults = cameraModel.getDefaults();
				discoveredCamera.setVendor(cameraModel.getVendorId());
			}
			else 
			{
				Model defaultModel = Vendor.getById(discoveredCamera.getVendor()).getDefaultModel();
				defaults = defaultModel.getDefaults();
				
				if(!discoveredCamera.hasModelThumbnailUrl())
				{
					discoveredCamera.setModelThumbnail(defaultModel.getThumbnailUrl());
				}
			}
			
			String username = defaults.getAuth(Auth.TYPE_BASIC).getUsername();
			String password = defaults.getAuth(Auth.TYPE_BASIC).getPassword();
			String jpgUrl = defaults.getJpgURL();
			String h264Url = defaults.getH264URL();
			
			discoveredCamera.setUsername(username);
			discoveredCamera.setPassword(password);
			discoveredCamera.setJpg(jpgUrl);
			discoveredCamera.setH264(h264Url);
			
			if(discoveredCamera.hasVendor())
			{
				discoveredCamera.setVendorThumbnail(EvercamQuery
						.getVendorThumbnailUrl(discoveredCamera.getVendor()));
			}
		}
		catch (EvercamException e)
		{
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		
		return discoveredCamera;
	}

	@Deprecated
	public static String getModelThumbnailUrl(String modelId)
	{
		if (!modelId.isEmpty())
		{
			try
			{
				Model model = Model.getById(modelId);
				return model.getThumbnailUrl();
			}
			catch (EvercamException e)
			{
				
			}
		}
		return "";
	}

	public static String getVendorThumbnailUrl(String vendorId)
	{
		if (!vendorId.isEmpty())
		{
			try
			{
				Vendor vendor = Vendor.getById(vendorId);
				return vendor.getLogoUrl();
			}
			catch (EvercamException e)
			{
				EvercamDiscover.printLogMessage("Vendor" + vendorId + " doesn't exist");
			}
		}
		return "";
	}
}
