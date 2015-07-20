package io.evercam.network.discovery;

import net.sbbi.upnp.devices.UPNPRootDevice;

public class UpnpDevice
{
	private String ip;
	private String model;
	private int port;
	private String friendlyName;

	protected UpnpDevice(UPNPRootDevice upnpRootDevice)
	{
		this.ip = UpnpDiscovery.getIPFromUpnp(upnpRootDevice);
		this.port = UpnpDiscovery.getPortFromUpnp(upnpRootDevice);
		this.model = UpnpDiscovery.getModelFromUpnp(upnpRootDevice);
		this.friendlyName = UpnpDiscovery.getFriendlyNameFromUpnp(upnpRootDevice);
	}

	public String getIp()
	{
		return ip;
	}

	public String getModel()
	{
		return model;
	}

	public int getPort()
	{
		return port;
	}

	public String getFriendlyName()
	{
		return friendlyName;
	}

	@Override
	public String toString()
	{
		return "UpnpDevice [ip=" + ip + ", model=" + model + ", port=" + port + ", friendlyName="
				+ friendlyName + "]";
	}
}
