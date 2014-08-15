package io.evercam.network.ipscan;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortScan
{
	PortScanResult portScanResult;
	public final int[] STANDARD_PORTS = { 20, 21, 22, 80, 443, 554 };
	public static final int TYPE_COMMON = 1;
	public static final int TYPE_STANDARD = 0;
	
	public PortScan(PortScanResult portScanResult)
	{
		this.portScanResult = portScanResult;
	}

	public static boolean isPortReachable(String ip, int port) throws Exception
	{
		try
		{
			InetAddress ip_net = InetAddress.getByName(ip);
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip_net, port), 1000);
			socket.close();
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	// scan both stand and common ports
	public void start(String ip) throws Exception
	{
		scanByStandard(ip, STANDARD_PORTS, 0);
		scanByStandard(ip, getCommonPorts(ip), 1);
	}

	// get common ports
	public int[] getCommonPorts(String ip)
	{
		int[] commonPorts = new int[2];
		String subIp = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
		int subIpInt = Integer.parseInt(subIp);
		int common_http = 8000 + subIpInt;
		int common_rtsp = 9000 + subIpInt;
		commonPorts[0] = common_http;
		commonPorts[1] = common_rtsp;
		return commonPorts;
	}

	public void scanByStandard(String ip, int[] ports, int type) throws Exception
	{
		// type = 0: stantard port
		// type = 1: common port
		int port;
		for (int i = 0; i < ports.length; i++)
		{
			port = ports[i];
			if (isPortReachable(ip, port))
			{
				portScanResult.onPortActive(port, type);
			}
		}
	}

}
