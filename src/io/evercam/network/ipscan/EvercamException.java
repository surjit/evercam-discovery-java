package io.evercam.network.ipscan;

public class EvercamException extends Exception
{

	private static final long serialVersionUID = 1L;
	protected final static String MSG_PORT_SCAN_NOT_STARTED = "Please launch port scan first";

	public EvercamException(String message)
	{
		super(message);
	}
}
