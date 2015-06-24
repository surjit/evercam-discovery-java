package io.evercam.network.discovery.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import io.evercam.network.discovery.PortScan;

import java.util.ArrayList;

import org.junit.Test;

public class PortScanTest 
{
	@Test
	public void testScanPort() throws Exception
	{
		PortScan portScan = new PortScan(null);
		portScan.start("10.0.0.36");
		ArrayList<Integer> activePortList = portScan.getActivePorts();
		
		for(Integer port : activePortList)
		{
			//System.out.println(port);
		}
		assertEquals(3, activePortList.size());
	}
	
	@Test
	public void testScanSinglePort() throws Exception
	{
		assertFalse(PortScan.isPortReachable("10.0.0.36", 8036));
	}
}
