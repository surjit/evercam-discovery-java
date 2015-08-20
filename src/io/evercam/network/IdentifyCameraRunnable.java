package io.evercam.network;

import io.evercam.Vendor;
import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.MacAddress;
import io.evercam.network.discovery.Port;
import io.evercam.network.discovery.PortScan;
import io.evercam.network.query.EvercamQuery;

import java.util.ArrayList;

public abstract class IdentifyCameraRunnable implements Runnable
{
	private String ip;

	public IdentifyCameraRunnable(String ip)
	{
		this.ip = ip;
	}

	@Override
	public void run()
	{
		EvercamDiscover.printLogMessage("Identifying : " + ip);
		try
		{
			String macAddress = MacAddress.getByIpLinux(ip);
			if (!macAddress.equals(Constants.EMPTY_MAC))
			{
				Vendor vendor = EvercamQuery.getCameraVendorByMac(macAddress);
				if (vendor != null)
				{
					String vendorId = vendor.getId();
					if (!vendorId.isEmpty())
					{
						EvercamDiscover.printLogMessage(ip
								+ " is identified as a camera, vendor is: " + vendorId);
						// Then fill details discovered from IP scan
						DiscoveredCamera camera = new DiscoveredCamera(ip);
						camera.setMAC(macAddress);
						camera.setVendor(vendorId);

						// Start port scan
						PortScan portScan = new PortScan();
						portScan.start(ip);
						ArrayList<Port> activePortList = portScan.getActivePorts();

						if (activePortList.size() > 0)
						{
							camera = camera.mergePorts(activePortList);
						}

						onCameraFound(camera, vendor);
					}
				}
			}
		}
		catch (Exception e)
		{
			if (Constants.ENABLE_LOGGING)
			{
				e.printStackTrace();
			}
		}
		
		EvercamDiscover.printLogMessage("Identification finished:  " + ip);

		onFinished();
	}

	public abstract void onCameraFound(DiscoveredCamera discoveredCamera, Vendor vendor);

	public abstract void onFinished();
}
