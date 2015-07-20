package io.evercam.network.discovery;

public interface ScanResult
{
	public void onActiveIp(String ip);

	public void onIpScanned(String ip);
}
