package io.evercam.network.upnp;

import net.sbbi.upnp.devices.UPNPRootDevice;

public interface UpnpResult
{
	public abstract void onUpnpDeviceFound(UPNPRootDevice upnpDevice);
}
