package io.evercam.network.ipscan;

public class ScanRange
{
	private long scanIp;
	private long scanStart;
	private long scanEnd;
	
	public ScanRange(String ip, String subnetMask)
	{
		scanIp = IpTranslator.getUnsignedLongFromIp(ip);

		int cidr = IpTranslator.maskIpToCidr(subnetMask);
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
	
	protected int countSize()
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
