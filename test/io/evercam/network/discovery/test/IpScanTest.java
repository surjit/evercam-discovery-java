package io.evercam.network.discovery.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.evercam.network.discovery.IpScan;
import io.evercam.network.discovery.ScanRange;
import io.evercam.network.discovery.ScanResult;

import org.junit.Test;

public class IpScanTest
{
//	private final String TEST_ACTIVE_IP = "192.168.1.1";
//	private String TEST_IP = "192.168.1.122";
	
	private final String TEST_ACTIVE_IP = "172.16.0.1";
	private String TEST_IP = "172.16.0.136";
	private final String TEST_NET_MASK = "255.255.255.0";
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

			@Override
			public void onIpScanned(String ip) {
				// TODO Auto-generated method stub
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
				System.out.println("IP added :" + ip);
			}

			@Override
			public void onIpScanned(String ip) {
				// TODO Auto-generated method stub
			}
		});
		//ipScan.scanAll(new ScanRange(NetworkInfo.getNetworkInterfaceByIp(TEST_IP)));
		ipScan.scanAll(new ScanRange(TEST_IP, TEST_NET_MASK));
		System.out.println(ipList.size());
		assertTrue(ipList.size() > 5);
		ipList.clear();
	}
	
	@Test
	public void testScanRange() throws Exception
	{
		ScanRange scanRange = new ScanRange(TEST_IP, TEST_NET_MASK);
		assertTrue(scanRange.containIp(TEST_ACTIVE_IP));
		assertFalse(scanRange.containIp("192.168.1.6"));
	}
}
