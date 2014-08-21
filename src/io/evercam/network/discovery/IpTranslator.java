package io.evercam.network.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpTranslator
{
	public static final String EMPTY_IP = "0.0.0.0";

	public static String getIpFromIntSigned(int ip_int)
	{
		String ip = "";
		for (int k = 0; k < 4; k++)
		{
			ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}

	public static String getIpFromLongUnsigned(long ip_long)
	{
		String ip = "";
		for (int k = 3; k > -1; k--)
		{
			ip = ip + ((ip_long >> k * 8) & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}

	protected static long getUnsignedLongFromIp(String ip_addr) throws Exception
	{

		if (ip_addr != null)
		{
			String[] a = ip_addr.split("\\.");
			return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
					+ Integer.parseInt(a[2]) * 256 + Integer.parseInt(a[3]));
		}
		else
		{
			throw new Exception("IP address can not be null");
		}
	}

	public static int maskIpToCidr(String ip)
	{
		double sum = -2;
		String[] part = ip.split("\\.");
		for (String p : part)
		{
			sum += 256D - Double.parseDouble(p);
		}
		return 32 - (int) (Math.log(sum) / Math.log(2d));
	}

	public static String cidrToMask(int cidr)
	{
		int value = 0xffffffff << (32 - cidr);
		byte[] bytes = new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff),
				(byte) (value >> 8 & 0xff), (byte) (value & 0xff) };

		InetAddress netAddr;
		try
		{
			netAddr = InetAddress.getByAddress(bytes);
			return netAddr.getHostAddress();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		return EMPTY_IP;
	}

}
