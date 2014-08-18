package io.evercam.network.ipscan;

import java.util.ArrayList;
import java.util.Locale;

import io.evercam.EvercamException;
import io.evercam.Vendor;
import io.evercam.network.camera.Constants;
import io.evercam.network.camera.DiscoveredCamera;

public class EvercamDiscover
{
	private ArrayList<String> activeIpList = new ArrayList<String>();

	public ArrayList<DiscoveredCamera> discoverAllCamerasAndroid(ScanRange scanRange)
			throws Exception
	{
		ArrayList<DiscoveredCamera> cameraList = new ArrayList<DiscoveredCamera>();

		// Request for external IP address
		String externalIp = NetworkInfo.getExternalIP();
		// Scan to get a list of active IP addresses.
		IpScan ipScan = new IpScan(new ScanResult(){
			@Override
			public void onActiveIp(String ip)
			{
				activeIpList.add(ip);
			}
		});
		ipScan.scanAll(scanRange);

		// For each active IP, request for MAC address and vendor
		for (String ip : activeIpList)
		{
			String macAddress = MacAddress.getByIpAndroid(ip);
			if (macAddress != Constants.EMPTY_MAC)
			{
				Vendor vendor = getVendorByMac(macAddress);
				if (vendor != null)
				{
					String vendorId = vendor.getId();
					if (!vendorId.isEmpty())
					{
						// Then fill details and do port scan.
						DiscoveredCamera camera = new DiscoveredCamera(ip);
						camera.setMAC(macAddress);
						camera.setVendor(vendorId);
						camera.setFlag(Constants.TYPE_CAMERA);
						camera.setExternalIp(externalIp);

						PortScan portScan = new PortScan(null);
						portScan.start(ip);
						ArrayList<Integer> activePortList = portScan.getActivePorts();
						addPortsInfoToCamera(camera, activePortList);
						cameraList.add(camera);
					}
				}
			}
		}

		return cameraList;
	}

	private DiscoveredCamera addPortsInfoToCamera(DiscoveredCamera camera,
			ArrayList<Integer> activePortList)
	{
		for (Integer port : activePortList)
		{
			String port_s = String.valueOf(port);
			if (port == 80)
			{
				camera.setHttp(port);
			}
			else if (port == 554)
			{
				camera.setRtsp(port);
			}
			else if (port == 443)
			{
				camera.setHttps(port);
			}
			else
			{
				if (port_s.startsWith("8"))
				{
					camera.setHttp(port);
				}
				else if (port_s.startsWith("9"))
				{
					camera.setRtsp(port);
				}
			}
		}
		return camera;
	}

	/**
	 * Query Evercam API to get camera vendor by MAC address.
	 * 
	 * @param macAddress
	 *            Full MAC address read from device.
	 */
	private Vendor getVendorByMac(String macAddress)
	{
		String submac = macAddress.substring(0, 8).toLowerCase(Locale.UK);

		try
		{
			return Vendor.getByMac(submac).get(0);
		}
		catch (EvercamException e)
		{
			return null;
		}
	}
}
