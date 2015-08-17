package io.evercam.network.discovery.test;

import static org.junit.Assert.*;
import io.evercam.network.discovery.IpTranslator;
import io.evercam.network.discovery.NetworkInfo;

import java.io.IOException;
import java.net.NetworkInterface;

import org.junit.Test;

public class NetworkInfoTest
{
	private final String TEST_IP = "10.0.0.25";
	private final String TEST_NET_MASK = "255.255.255.0";
	private final String TEST_EXTERNAL_IP = "5.149.169.19";
	private final int TEST_CIDR = 24;

	@Test
	public void testGetCidr() throws IOException
	{
		NetworkInterface networkInterface = NetworkInfo.getNetworkInterfaceByIp(TEST_IP);
		int cidr = NetworkInfo.getCidrFromInterface(networkInterface);
		assertEquals(TEST_CIDR, cidr);
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
	
	@Test
	public void testValidIpv4()
	{
		String ipv41 = "192.168.1.1";
		String ipv42 = "172.0.0.6";
		String ipv43 = "89.101.130.1";
		String wrongIpv4 = "(192.168.1.1)";
		String ipv6 = "FE80:0000:0000:0000:0202:B3FF";
		String wrongIpv6 = "(FE80:0000:0000:0000:0202:B3FF)";
		
		assertTrue(IpTranslator.isValidIpv4Addr(ipv41));
		assertTrue(IpTranslator.isValidIpv4Addr(ipv42));
		assertTrue(IpTranslator.isValidIpv4Addr(ipv43));
		assertFalse(IpTranslator.isValidIpv4Addr(wrongIpv4));
		assertFalse(IpTranslator.isValidIpv4Addr(ipv6));
		assertFalse(IpTranslator.isValidIpv4Addr(wrongIpv6));
	}
}
