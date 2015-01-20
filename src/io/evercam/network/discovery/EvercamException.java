package io.evercam.network.discovery;

public class EvercamException extends Exception
{

	private static final long serialVersionUID = 1L;
	protected final static String MSG_PORT_SCAN_NOT_STARTED = "Please launch port scan first";
	protected static final String MSG_UPNP_NOT_STARTED = "Please launch UPnP discovery first";

	public EvercamException(String message)
	{
		super(message);
	}
}
