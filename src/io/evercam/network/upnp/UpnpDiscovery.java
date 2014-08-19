package io.evercam.network.upnp;

import io.evercam.network.camera.Constants;
import io.evercam.network.ipscan.EvercamException;

import java.io.IOException;
import java.util.ArrayList;

import net.sbbi.upnp.Discovery;
import net.sbbi.upnp.devices.UPNPRootDevice;

public class UpnpDiscovery
{
	private UpnpResult upnpResult;
	private UPNPRootDevice[] devices = null;
	private ArrayList<UpnpDevice> upnpDeviceList;
	public static final String DEFAULT_DEVICE_TYPE = "upnp:rootdevice";

	// UPnP keys
	public static final String UPNP_KEY_INTERNAL_PORT = "NewInternalPort";
	public static final String UPNP_KEY_EXTERNAL_PORT = "NewExternalPort";
	public static final String UPNP_KEY_DESCRIPTION = "NewPortMappingDescription";
	public static final String UPNP_KEY_PROTOCOL = "NewProtocol";
	public static final String UPNP_KEY_INTERNAL_CLIENT = "NewInternalClient";

	public UpnpDiscovery(UpnpResult upnpResult)
	{
		this.upnpResult = upnpResult;
	}

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
							upnpResult.onUpnpDeviceFound(devices[i]);
						}
						else
						{
							upnpDeviceList.add(new UpnpDevice(devices[i]));
						}
					}
				}
			}
			else
			{
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<UpnpDevice> getUpnpDevices() throws EvercamException
	{
		if (upnpDeviceList != null)
		{
			return upnpDeviceList;
		}
		else
		{
			throw new EvercamException(Constants.MSG_UPNP_NOT_STARTED);
		}
	}

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

	public static int getPortFromUpnp(UPNPRootDevice upnpDevice)
	{
		if (upnpDevice.getPresentationURL() != null)
		{
			return upnpDevice.getPresentationURL().getPort();
		}

		return 0;
	}

	public static String getModelFromUpnp(UPNPRootDevice upnpDevice)
	{
		String modelName = upnpDevice.getModelName();
		return modelName;
	}
}
