package io.evercam.network;

import java.util.ArrayList;
import java.util.Locale;

import net.sbbi.upnp.messages.ActionResponse;

import io.evercam.EvercamException;
import io.evercam.Vendor;
import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.GatewayDevice;
import io.evercam.network.discovery.IpScan;
import io.evercam.network.discovery.MacAddress;
import io.evercam.network.discovery.NetworkInfo;
import io.evercam.network.discovery.PortScan;
import io.evercam.network.discovery.ScanRange;
import io.evercam.network.discovery.ScanResult;
import io.evercam.network.discovery.UpnpDevice;
import io.evercam.network.discovery.UpnpDiscovery;

public class EvercamDiscover
{
	private ArrayList<String> activeIpList = new ArrayList<String>();

	/**
	 * The wrapped method to scan for cameras in Android platform.
	 * 
	 * @param scanRange
	 *            the range of IP addresses to scan
	 * @param routerIp
	 *            gateway/router IP address
	 * @return a list of discovered camera devices
	 * @throws Exception
	 */
	public ArrayList<DiscoveredCamera> discoverAllCamerasAndroid(ScanRange scanRange,
			String routerIp) throws Exception
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

		// Start UPnP discovery
		ArrayList<UpnpDevice> deviceList = new ArrayList<UpnpDevice>();
		try
		{
			UpnpDiscovery upnpDiscovery = new UpnpDiscovery(null);
			upnpDiscovery.discoverAll();
			deviceList = upnpDiscovery.getUpnpDevices();
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}

		// Start UPnP router discovery
		ArrayList<ActionResponse> mapEntries = new ArrayList<ActionResponse>();
		try
		{
			GatewayDevice gatewayDevice = new GatewayDevice(routerIp);
			mapEntries = gatewayDevice.getNatTableArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
						// Then fill details discovered from IP scan
						DiscoveredCamera camera = new DiscoveredCamera(ip);
						camera.setMAC(macAddress);
						camera.setVendor(vendorId);
						camera.setFlag(Constants.TYPE_CAMERA);
						camera.setExternalIp(externalIp);

						// Start port scan
						PortScan portScan = new PortScan(null);
						portScan.start(ip);
						ArrayList<Integer> activePortList = portScan.getActivePorts();

						// Add active ports to camera object
						camera = addPortsInfoToCamera(camera, activePortList);

						// Add details discovered from UPnP to camera object
						camera = mergeUpnpDeviceToCamera(camera, deviceList);

						// Add details in discovered NAT table(mainly forwarded ports) 
						camera = mergeNatTableToCamera(camera, mapEntries);
						
						cameraList.add(camera);
					}
				}
			}
		}

		return cameraList;
	}

	private DiscoveredCamera mergeUpnpDeviceToCamera(DiscoveredCamera camera,
			ArrayList<UpnpDevice> upnpDeviceList)
	{
		try
		{
		if(upnpDeviceList.size() > 0)
		{
			for (UpnpDevice upnpDevice : upnpDeviceList)
			{
				// If IP address matches
				String ipFromUpnp = upnpDevice.getIp();
				if (ipFromUpnp != null && !ipFromUpnp.isEmpty())
				{
					if (camera.getIP().equals(ipFromUpnp))
					{
						int port = upnpDevice.getPort();
						String model = upnpDevice.getModel();
						if (port != 0)
						{
							camera.setHttp(port);
						}
						camera.setModel(model);
					}
				}
			}
		}
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace().toString());
		}
		return camera;
	}

	private DiscoveredCamera addPortsInfoToCamera(DiscoveredCamera camera,
			ArrayList<Integer> activePortList)
	{
		if(activePortList.size() > 0)
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
		}
		return camera;
	}
	
	private DiscoveredCamera mergeNatTableToCamera(DiscoveredCamera camera, ArrayList<ActionResponse> mapEntries)
	{
		if(mapEntries.size() > 0)
		{
		for(ActionResponse mapEntry : mapEntries)
		{
			String natIp = mapEntry
					.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_INTERNAL_CLIENT);
			int natInternalPort = Integer.parseInt(mapEntry
					.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_INTERNAL_PORT));
			int natExternalPort = Integer.parseInt(mapEntry
					.getOutActionArgumentValue(UpnpDiscovery.UPNP_KEY_EXTERNAL_PORT));
			
			if(camera.getIP().equals(natIp))
			{
				if(camera.getHttp() == natInternalPort)
				{
					camera.setExthttp(natExternalPort);
				}
				if(camera.getRtsp() == natInternalPort)
				{
					camera.setExtrtsp(natExternalPort);
				}
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
