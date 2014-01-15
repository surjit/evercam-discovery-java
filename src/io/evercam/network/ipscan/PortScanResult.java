package io.evercam.network.ipscan;

public interface PortScanResult
{
	public void onPortActive(int port, int type);
}
