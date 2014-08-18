package io.evercam.network.ipscan.test;

import static org.junit.Assert.*;
import io.evercam.network.ipscan.IpTranslator;
import io.evercam.network.ipscan.NetworkInfo;

import java.io.IOException;
import java.net.NetworkInterface;

import org.junit.Test;

public class NetworkInfoTest
{
	private final String TEST_IP = "192.168.1.122";
	private final String TEST_NET_MASK = "255.255.255.0";
	private final String TEST_EXTERNAL_IP = "89.101.200.163";
	private final int TEST_CIDR = 24;

	@Test
	public void testGetCidr() throws IOException
	{
		NetworkInterface networkInterface = NetworkInfo.getNetworkInterfaceByIp(TEST_IP);
		int cidr = NetworkInfo.getCidrFromInterface(networkInterface);
		assertEquals(TEST_CIDR,cidr);
		assertEquals(TEST_NET_MASK, IpTranslator.cidrToMask(cidr));
	}
	
	@Test
	public void testGetTranslateCidrToMask() throws IOException
	{
		assertEquals(TEST_NET_MASK, IpTranslator.cidrToMask(TEST_CIDR));
		assertEquals(TEST_CIDR, IpTranslator.maskIpToCidr(TEST_NET_MASK));
	}
	
	@Test
	public void testExternalIp()
	{
		assertEquals(TEST_EXTERNAL_IP, NetworkInfo.getExternalIP());
	}
}
