package io.evercam.network.discovery;

import java.util.ArrayList;

public class PortScan
{
	private ArrayList<Port> activePortList;
	private PortScanCallback portScanCallback;

	public PortScan(PortScanCallback portScanCallback)
	{
		this.portScanCallback = portScanCallback;
	}
	
	public PortScan()
	{
		
	}

	// scan both stand and common ports
	public void start(String ip) throws Exception
	{
		activePortList = new ArrayList<Port>();
		scanStandardPorts(ip);
		scanCommonPorts(ip);
	}
	
	private ArrayList<Port> getStandardPortList()
	{
		ArrayList<Port> portList = new ArrayList<>();
		portList.add(new Port(Port.TYPE_HTTP, 80));
		portList.add(new Port(Port.TYPE_RTSP, 554));
		return portList;
	}
	
	private void scanPort(String ip, Port port) throws Exception
	{
		if (Port.isReachable(ip, port.getValue()))
		{
			// System.out.println("Active port added: " + ip + ":" + port);
			port.isActive();
			activePortList.add(port);

			if(portScanCallback != null)
			{
				portScanCallback.onActivePort(port);
			}
		}
	}

	private void scanStandardPorts(String ip) throws Exception
	{
		ArrayList<Port> standardPorts = getStandardPortList();

		for (Port port : standardPorts)
		{
			scanPort(ip,  port);
		}
	}
	
	/**
	 * Scan the common ports that are frequently used by Evercam 
	 * only when HTTP port(80) or RTSP port(554) is not open
	 */
	private void scanCommonPorts(String ip) throws Exception
	{
		String subIp = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
		int subIpInt = Integer.parseInt(subIp);
		
		ArrayList<String> activePortTypes = new ArrayList<>();
		for(Port port : activePortList)
		{
			activePortTypes.add(port.getType());
		}
		
		if(!activePortTypes.contains(Port.TYPE_HTTP))
		{
			Port commonHttpPort = new Port(Port.TYPE_HTTP, 8000 + subIpInt);
			scanPort(ip, commonHttpPort);
		}
		if(!activePortTypes.contains(Port.TYPE_HTTP))
		{
			Port commonRtspPort = new Port(Port.TYPE_RTSP, 9000 + subIpInt);
			scanPort(ip, commonRtspPort);
		}
	}

	public ArrayList<Port> getActivePorts() throws EvercamException
	{
		if (activePortList != null)
		{
			return activePortList;
		}
		else
		{
			throw new EvercamException(EvercamException.MSG_PORT_SCAN_NOT_STARTED);
		}
	}
}
