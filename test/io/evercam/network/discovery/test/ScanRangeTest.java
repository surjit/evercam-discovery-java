package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import io.evercam.network.discovery.ScanRange;

import org.junit.Test;

public class ScanRangeTest
{
	private String TEST_IP = "192.168.1.122";
	private String TEST_MASK = "255.255.255.0";
	private int TEST_RANGE_SIZE = 254;

	@Test
	public void testSetRangeByIpAndMask() throws Exception
	{
		ScanRange scanRange = new ScanRange(TEST_IP, TEST_MASK);
		assertEquals(TEST_RANGE_SIZE, scanRange.size());
	}

//	@Test
//	public void testSetRangeByInterface() throws Exception
//	{
//		NetworkInterface networkInterface = NetworkInfo.getNetworkInterfaceByIp(TEST_IP);
//		ScanRange scanRange = new ScanRange(networkInterface);
//		assertEquals(TEST_RANGE_SIZE, scanRange.size());
//	}
}
