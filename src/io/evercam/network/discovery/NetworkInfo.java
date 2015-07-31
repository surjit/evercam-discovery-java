package io.evercam.network.discovery;

import io.evercam.network.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

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
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		return interfaceNameArrayList;
	}

	/**
	 * Return network interface by interface name. Return null if no interface
	 * matches the given name.
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
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Return network interface by host IP address. Return null if no interface
	 * matches the given IP
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
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Return the network prefix length. Return 0 if no CIDR detected. FIXME:
	 * This method may return -1, which means it may not be the right approach
	 */
	public static int getCidrFromInterface(NetworkInterface networkInterface) throws IOException
	{
		for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
		{
			InetAddress inetAddress = address.getAddress();
			if (!inetAddress.isLoopbackAddress())
			{
				if (inetAddress instanceof Inet4Address)
				{
					return address.getNetworkPrefixLength();
				}
			}
		}
		return 0;
	}

	/**
	 * Return the valid ipv4 address for the given network interface. Return
	 * empty string if IP address available.
	 */
	public static String getIpFromInterface(NetworkInterface networkInterface) throws IOException
	{
		for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
		{
			InetAddress inetAddress = address.getAddress();
			if (!inetAddress.isLoopbackAddress())
			{
				if (inetAddress instanceof Inet4Address)
				{
					return inetAddress.getHostAddress();
				}
			}
		}
		return "";
	}

	public static String getExternalIP()
	{
		String extIP = "";
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
		try
		{
			HttpGet httpget = new HttpGet(Constants.URL_GET_EXTERNAL_ADDR);
			HttpResponse response;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				extIP = EntityUtils.toString(entity);
			}
		}
		catch (IOException e)
		{
			if(Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		finally
		{
			httpclient.getConnectionManager().shutdown();
		}
		return (extIP == "" ? "" : extIP.replace("\n", ""));
	}

	/**
	 * Run command 'netstat -rn' and abstract router IP
	 * 
	 * Example of Kernel IP routing table Destination Gateway Genmask Flags MSS
	 * Window irtt Iface 0.0.0.0 192.168.1.1 0.0.0.0 UG 0 0 0 eth0 192.168.1.0
	 * 0.0.0.0 255.255.255.0 U 0 0 0 eth0
	 * 
	 * Return router IP in Linux system. Return empty string if exception
	 * occurred.
	 */
	// FIXME: netstat -rn doesn't work when Internet is not connected
	public static String getLinuxRouterIp()
	{
		try
		{
			Process result = Runtime.getRuntime().exec("netstat -rn");

			BufferedReader output = new BufferedReader(new InputStreamReader(
					result.getInputStream()));

			String line = output.readLine();
			while (line != null)
			{
				if (line.startsWith("0.0.0.0"))
				{
					break;
				}
				line = output.readLine();
			}

			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			return st.nextToken();
		}
		catch (Exception e)
		{
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
			return "";
		}
	}

	/**
	 * Run command 'netstat -rn' and abstract subnet mask
	 * 
	 * Example of Kernel IP routing table Destination Gateway Genmask Flags MSS
	 * Window irtt Iface 0.0.0.0 192.168.1.1 0.0.0.0 UG 0 0 0 eth0 192.168.1.0
	 * 0.0.0.0 255.255.255.0 U 0 0 0 eth0
	 * 
	 * Return subnet mask in Linux system. Return empty string if exception
	 * occurred.
	 */
	public static String getLinuxSubnetMask()
	{
		try
		{
			Process result = Runtime.getRuntime().exec("netstat -rn");

			BufferedReader output = new BufferedReader(new InputStreamReader(
					result.getInputStream()));

			String line = output.readLine();
			while (line != null)
			{
				StringTokenizer st = new StringTokenizer(line);
				st.nextToken();
				String gateway = st.nextToken();
				if (gateway.equals("0.0.0.0"))
				{
					return st.nextToken();
				}
				line = output.readLine();
			}
		}
		catch (Exception e)
		{
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
}
