package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.IpScan;
import io.evercam.network.discovery.NetworkInfo;
import io.evercam.network.discovery.ScanRange;
import io.evercam.network.discovery.ScanResult;

import org.junit.Test;

public class IpScanTest
{
	private final String TEST_ACTIVE_IP = "192.168.1.1";
	private String TEST_IP = "192.168.1.122";
	ArrayList<String> ipList = new ArrayList<String>();

	@Test
	public void testScanSingleIp()
	{
		IpScan ipScan = new IpScan(new ScanResult(){
			@Override
			public void onActiveIp(String ip)
			{
				ipList.add(ip);

			}
		});
		ipScan.scanSingleIp(TEST_ACTIVE_IP, 3000);
		assertEquals(TEST_ACTIVE_IP, ipList.get(0));
		ipList.clear();
	}

	@Test
	public void testScanAllIp() throws Exception
	{
		IpScan ipScan = new IpScan(new ScanResult(){
			@Override
			public void onActiveIp(String ip)
			{
				ipList.add(ip);

			}
		});
		ipScan.scanAll(new ScanRange(NetworkInfo.getNetworkInterfaceByIp(TEST_IP)));
		assertTrue(ipList.size() > 5);
		ipList.clear();
	}
}
