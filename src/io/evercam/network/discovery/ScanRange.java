package io.evercam.network.discovery;

public class ScanRange
{
	private long routerIp;
	private String routerIpString;
	private long scanStart;
	private long scanEnd;
	private int cidr;

	public ScanRange(String routerIp, String subnetMask) throws Exception
	{
		this.routerIp = IpTranslator.getUnsignedLongFromIp(routerIp);
		this.routerIpString = routerIp;

		cidr = IpTranslator.maskIpToCidr(subnetMask);
		setUpStartAndEnd(cidr);
	}

	// TODO: Temporary disabled this because get cidr from interface is not
	// working sometimes
	// public ScanRange(NetworkInterface networkInterface) throws Exception
	// {
	// scanIp = IpTranslator.getUnsignedLongFromIp(NetworkInfo
	// .getIpFromInterface(networkInterface));
	// int cidr = NetworkInfo.getCidrFromInterface(networkInterface);
	// setUpStartAndEnd(cidr);
	// }

	private void setUpStartAndEnd(int cidr)
	{
		int shift = (32 - cidr);
		if (cidr < 31)
		{
			scanStart = (routerIp >> shift << shift) + 1;
			scanEnd = (scanStart | ((1 << shift) - 1)) - 1;
		}
		else
		{
			scanStart = (routerIp >> shift << shift);
			scanEnd = (scanStart | ((1 << shift) - 1));
		}
	}
	
	/**
	 * @return true if the given IP is in this scan range
	 */
	public boolean containIp(String ip) throws Exception
	{
		int shift = (32 - cidr);
		long ipLong = IpTranslator.getUnsignedLongFromIp(ip);
		return scanStart == ((ipLong >> shift << shift) + 1);
	}

	public int size()
	{
		return (int) (scanEnd - scanStart + 1);
	}

	protected long getRouterIp()
	{
		return routerIp;
	}

	public String getRouterIpString()
	{
		return routerIpString;
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
		this.routerIp = scanIp;
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
