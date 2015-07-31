package io.evercam.network.discovery;

import io.evercam.network.Constants;

import java.io.IOException;
import java.util.ArrayList;

import net.sbbi.upnp.Discovery;
import net.sbbi.upnp.devices.UPNPRootDevice;

/**
 * Discover UPnP root device within the network
 */
public class UpnpDiscovery
{
	private UpnpResult upnpResult;
	private UPNPRootDevice[] devices = null;
	private ArrayList<UpnpDevice> upnpDeviceList;
	public static final String DEFAULT_DEVICE_TYPE = "upnp:rootdevice";

	public UpnpDiscovery(UpnpResult upnpResult)
	{
		this.upnpResult = upnpResult;
	}

	/**
	 * Discover all UPnP device with device type - upnp:rootdevice
	 */
	public void discoverAll()
	{
		upnpDeviceList = new ArrayList<UpnpDevice>();
		try
		{
			devices = Discovery.discover(Discovery.DEFAULT_TIMEOUT, Discovery.DEFAULT_TTL,
					Discovery.DEFAULT_MX, DEFAULT_DEVICE_TYPE, null);
			if (devices != null)
			{
				for (int i = 0; i < devices.length; i++)
				{
					if (devices[i] != null)
					{
						if (upnpResult != null)
						{
							upnpResult.onUpnpDeviceFound(new UpnpDevice(devices[i]));
						}
						else
						{
							upnpDeviceList.add(new UpnpDevice(devices[i]));
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return a list of discovered UPnP device
	 * @throws EvercamException
	 *             if the discovery not started yet
	 */
	public ArrayList<UpnpDevice> getUpnpDevices() throws EvercamException
	{
		if (upnpDeviceList != null)
		{
			return upnpDeviceList;
		}
		else
		{
			throw new EvercamException(EvercamException.MSG_UPNP_NOT_STARTED);
		}
	}

	/**
	 * @param upnpDevice
	 *            discovered UPNPRootDevice
	 * @return the IP address of the UPnP device
	 */
	public static String getIPFromUpnp(UPNPRootDevice upnpDevice)
	{
		if (upnpDevice.getPresentationURL() != null)
		{
			return upnpDevice.getPresentationURL().getHost();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param upnpDevice
	 *            discovered UPNPRootDevice
	 * @return the HTTP port of the device, if not exists, return 0
	 */
	public static int getPortFromUpnp(UPNPRootDevice upnpDevice)
	{
		if (upnpDevice.getPresentationURL() != null)
		{
			return upnpDevice.getPresentationURL().getPort();
		}

		return 0;
	}

	/**
	 * @param upnpDevice
	 *            discovered UPNPRootDevice
	 * @return the model of the UPnP device
	 */
	public static String getModelFromUpnp(UPNPRootDevice upnpDevice)
	{
		String modelName = upnpDevice.getModelName();
		return modelName;
	}

	public static String getFriendlyNameFromUpnp(UPNPRootDevice upnpDevice)
	{
		return upnpDevice.getFriendlyName();
	}
}
