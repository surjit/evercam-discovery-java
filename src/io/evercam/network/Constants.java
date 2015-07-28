package io.evercam.network;

public class Constants
{
	private Constants()
	{
	}

	public static boolean ENABLE_LOGGING = false;

	// device types
	public static final int TYPE_CAMERA = 1;
	public static final int TYPE_ROUTER = 2;
	public static final int TYPE_OTHERS = 3;

	public static final String EMPTY_MAC = "00:00:00:00:00:00";

	public static final String URL_GET_EXTERNAL_ADDR = "http://ipinfo.io/ip";
}
