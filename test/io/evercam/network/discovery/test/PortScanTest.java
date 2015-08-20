package io.evercam.network.discovery.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import io.evercam.network.discovery.Port;
import io.evercam.network.discovery.PortScan;

import java.util.ArrayList;

import org.junit.Test;

public class PortScanTest 
{
	@Test
	public void testScanPort() throws Exception
	{
		PortScan portScan = new PortScan();
		portScan.start("10.0.0.36");
		ArrayList<Port> activePortList = portScan.getActivePorts();
		
		for(Port port : activePortList)
		{
			//System.out.println(port.getValue());
		}
		assertEquals(3, activePortList.size());
	}
	
	@Test
	public void testScanSinglePort() throws Exception
	{
		assertFalse(Port.isReachable("10.0.0.36", 8036));
	}
}
