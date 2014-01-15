package io.evercam.network.ipscan;

import java.net.Inet6Address;
import java.net.InetAddress;
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
				NetworkInterface networkInterface = networkInterfaceEnum
						.nextElement();
				for (Enumeration<InetAddress> nis = networkInterface
						.getInetAddresses(); nis.hasMoreElements();)
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
							interfaceNameArrayList.add(networkInterface
									.getName());
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

}
