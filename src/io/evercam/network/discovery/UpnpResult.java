package io.evercam.network.discovery;

import net.sbbi.upnp.devices.UPNPRootDevice;

public interface UpnpResult
{
	public abstract void onUpnpDeviceFound(UPNPRootDevice upnpDevice);
}
