package io.evercam.network.discovery;

import net.sbbi.upnp.messages.ActionResponse;

public class NatMapEntry
{
	// UPnP NAT keys
	public static final String KEY_INTERNAL_PORT = "NewInternalPort";
	public static final String KEY_EXTERNAL_PORT = "NewExternalPort";
	public static final String KEY_DESCRIPTION = "NewPortMappingDescription";
	public static final String KEY_PROTOCOL = "NewProtocol";
	public static final String KEY_INTERNAL_CLIENT = "NewInternalClient";

	private ActionResponse actionResponse;

	public NatMapEntry(ActionResponse actionResponse)
	{
		this.actionResponse = actionResponse;
	}

	public String getIpAddress()
	{
		return actionResponse.getOutActionArgumentValue(KEY_INTERNAL_CLIENT);
	}

	public int getInternalPort()
	{
		return Integer.parseInt(actionResponse.getOutActionArgumentValue(KEY_INTERNAL_PORT));
	}

	public int getExternalPort()
	{
		return Integer.parseInt(actionResponse.getOutActionArgumentValue(KEY_EXTERNAL_PORT));
	}

	public String getDescription()
	{
		return actionResponse.getOutActionArgumentValue(KEY_DESCRIPTION);
	}

	public String getProtocal()
	{
		return actionResponse.getOutActionArgumentValue(KEY_PROTOCOL);
	}
}
