package io.evercam.network.discovery;

import java.net.NetworkInterface;

public class ScanRange
{
	private long scanIp;
	private long scanStart;
	private long scanEnd;

	public ScanRange(String ip, String subnetMask) throws Exception
	{
		scanIp = IpTranslator.getUnsignedLongFromIp(ip);

		int cidr = IpTranslator.maskIpToCidr(subnetMask);
		setUpStartAndEnd(cidr);
	}

	//TODO: Temporary disabled this because get cidr from interface is not working sometimes
//	public ScanRange(NetworkInterface networkInterface) throws Exception
//	{
//		scanIp = IpTranslator.getUnsignedLongFromIp(NetworkInfo
//				.getIpFromInterface(networkInterface));
//		int cidr = NetworkInfo.getCidrFromInterface(networkInterface);
//		setUpStartAndEnd(cidr);
//	}

	private void setUpStartAndEnd(int cidr)
	{
		int shift = (32 - cidr);
		if (cidr < 31)
		{
			scanStart = (scanIp >> shift << shift) + 1;
			scanEnd = (scanStart | ((1 << shift) - 1)) - 1;
		}
		else
		{
			scanStart = (scanIp >> shift << shift);
			scanEnd = (scanStart | ((1 << shift) - 1));
		}
	}

	public int size()
	{
		return (int) (scanEnd - scanStart + 1);
	}

	protected long getScanIp()
	{
		return scanIp;
	}

	protected long getScanStart()
	{
		return scanStart;
	}

	protected long getScanEnd()
	{
		return scanEnd;
	}

	protected void setScanIp(long scanIp)
	{
		this.scanIp = scanIp;
	}

	protected void setScanStart(long scanStart)
	{
		this.scanStart = scanStart;
	}

	protected void setScanEnd(long scanEnd)
	{
		this.scanEnd = scanEnd;
	}

}
