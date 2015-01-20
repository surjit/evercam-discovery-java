package io.evercam.network.query;

import java.util.ArrayList;
import java.util.Locale;

import io.evercam.Auth;
import io.evercam.Defaults;
import io.evercam.EvercamException;
import io.evercam.Vendor;

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
			if(vendorList.size()>0)
			{
				return Vendor.getByMac(submac).get(0);
			}
		}
		catch (EvercamException e)
		{
			System.out.println(e.toString());
			return null;
		}
		return null;
	}
	
	private static Defaults getDefaultsByVendor(Vendor vendor) throws EvercamException
	{
			return vendor.getDefaultModel().getDefaults();
	}
	
	/**
	 * @param vendor Camera vendor returned from Evercam
	 * @return the default username of the specified vendor
	 * @throws EvercamException if no default values associated with this vendor
	 */
	public static String getDefaultUsernameByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		
		return defaults.getAuth(Auth.TYPE_BASIC).getUsername();
	}
	
	/**
	 * @param vendor Camera vendor returned from Evercam
	 * @return the default password of the specified vendor
	 * @throws EvercamException if no default values associated with this vendor
	 */
	public static String getDefaultPasswordByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		
		return defaults.getAuth(Auth.TYPE_BASIC).getPassword();
	}
	
	/**
	 * @param vendor Camera vendor returned from Evercam
	 * @return the default JPG snapshot URL of the specified vendor
	 * @throws EvercamException if no default values associated with this vendor
	 */
	public static String getDefaultJpgUrlByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		
		return defaults.getJpgURL();
	}
	
	/**
	 * @param vendor Camera vendor returned from Evercam
	 * @return the default h264 stream URL of the specified vendor
	 * @throws EvercamException if no default values associated with this vendor
	 */
	public static String getDefaultH264UrlByVendor(Vendor vendor) throws EvercamException
	{
		Defaults defaults = getDefaultsByVendor(vendor);
		
		return defaults.getH264URL();
	}
	
	
}
