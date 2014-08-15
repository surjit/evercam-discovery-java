package io.evercam.network.ipscan;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class NetworkInfo
{

	public static ArrayList<String> getNetworkInterfaceNames()
	{
		Enumeration<NetworkInterface> networkInterfaces = null;
		ArrayList<String> interfaceNameArrayList = new ArrayList<String>();
		try
		{
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (Enumeration<NetworkInterface> networkInterfaceEnum = networkInterfaces; networkInterfaces
					.hasMoreElements();)
			{
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				for (Enumeration<InetAddress> nis = networkInterface.getInetAddresses(); nis
						.hasMoreElements();)
				{
					InetAddress thisInetAddress = nis.nextElement();
					if (!thisInetAddress.isLoopbackAddress())
					{
						if (thisInetAddress instanceof Inet6Address)
						{
							continue;
						}
						else
						{
							interfaceNameArrayList.add(networkInterface.getName());
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return interfaceNameArrayList;
	}

	/**
	 * Return network interface by interface name.
	 * Return null if no interface matches the given name.
	 */
	public static NetworkInterface getNetworkInterfaceByName(String interfaceName)
	{
		Enumeration<NetworkInterface> networkInterfaces = null;
		try
		{
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (Enumeration<NetworkInterface> networkInterfaceEnum = networkInterfaces; networkInterfaces
					.hasMoreElements();)
			{
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				for (Enumeration<InetAddress> nis = networkInterface.getInetAddresses(); nis
						.hasMoreElements();)
				{
					InetAddress thisInetAddress = nis.nextElement();
					if (!thisInetAddress.isLoopbackAddress())
					{
						if (thisInetAddress instanceof Inet6Address)
						{
							continue;
						}
						else
						{
							if (networkInterface.getName().equals(interfaceName))
							{
								return networkInterface;
							}
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return network interface by host IP address.
	 * Return null if no interface matches the given IP
	 */
	public static NetworkInterface getNetworkInterfaceByIp(String ipAddress)
	{
		Enumeration<NetworkInterface> networkInterfaces = null;
		try
		{
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (Enumeration<NetworkInterface> networkInterfaceEnum = networkInterfaces; networkInterfaces
					.hasMoreElements();)
			{
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				for (Enumeration<InetAddress> nis = networkInterface.getInetAddresses(); nis
						.hasMoreElements();)
				{
					InetAddress thisInetAddress = nis.nextElement();
					if (!thisInetAddress.isLoopbackAddress())
					{
						if (thisInetAddress instanceof Inet6Address)
						{
							continue;
						}
						else
						{
							if (thisInetAddress.getHostAddress().equals(ipAddress))
							{
								return networkInterface;
							}
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return the network prefix length.
	 * Return 0 if no CIDR detected.
	 */
	 public static int getCidrFromInterface(NetworkInterface networkInterface) throws IOException
	 {
		 for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
		 {
			 InetAddress inetAddress= address.getAddress();
			 if(!inetAddress.isLoopbackAddress())
			 {
				 if(inetAddress instanceof Inet4Address)
				 {
					 return address.getNetworkPrefixLength();
				 }
			 }
		 } 
		 return 0;
	 }
	 
		/**
		 * Return the valid ipv4 address for the given network interface.
		 * Return empty string if IP address available.
		 */
		 public static String getIpFromInterface(NetworkInterface networkInterface) throws IOException
		 {
			 for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
			 {
				 InetAddress inetAddress= address.getAddress();
				 if(!inetAddress.isLoopbackAddress())
				 {
					 if(inetAddress instanceof Inet4Address)
					 {
						 return inetAddress.getHostAddress();
					 }
				 }
			 } 
			 return "";
		 }
}
