package io.evercam.network.discovery;

import net.sbbi.upnp.devices.UPNPRootDevice;

public class UpnpDevice
{
	private String ip;
	private String model;
	private int port;

	protected UpnpDevice(UPNPRootDevice upnpRootDevice)
	{
		this.ip = UpnpDiscovery.getIPFromUpnp(upnpRootDevice);
		this.port = UpnpDiscovery.getPortFromUpnp(upnpRootDevice);
		this.model = UpnpDiscovery.getModelFromUpnp(upnpRootDevice);
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

	@Override
	public String toString()
	{
		return "UpnpDevice [ip=" + ip + ", model=" + model + ", port=" + port + "]";
	}
}
