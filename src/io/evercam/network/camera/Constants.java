package io.evercam.network.camera;

public class Constants
{
	// device types
	public static final int TYPE_CAMERA = 1;
	public static final int TYPE_ROUTER = 2;
	public static final int TYPE_OTHERS = 3;

	public static final String EMPTY_MAC = "00:00:00:00:00:00";

	public static final String URL_GET_EXTERNAL_ADDR = "http://ipinfo.io/ip";

	// Exception messages
	public static final String MSG_UPNP_NOT_STARTED = "Please launch UPnP discovery first";
}
