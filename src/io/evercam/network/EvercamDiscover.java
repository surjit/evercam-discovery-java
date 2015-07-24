package io.evercam.network;

import io.evercam.Auth;
import io.evercam.Defaults;
import io.evercam.EvercamException;
import io.evercam.Vendor;
import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.IpScan;
import io.evercam.network.discovery.NatMapEntry;
import io.evercam.network.discovery.NetworkInfo;
import io.evercam.network.discovery.ScanRange;
import io.evercam.network.discovery.ScanResult;
import io.evercam.network.discovery.UpnpDevice;
import io.evercam.network.query.EvercamQuery;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EvercamDiscover
{
	public static final int DEFAULT_FIXED_POOL = 20;

	private ArrayList<String> activeIpList = new ArrayList<String>();
	private ArrayList<UpnpDevice> deviceList = new ArrayList<UpnpDevice>();// UPnP
																			// device
																			// list
	private ArrayList<NatMapEntry> mapEntries = new ArrayList<NatMapEntry>();// NAT
																				// table
	private ArrayList<DiscoveredCamera> cameraList = new ArrayList<DiscoveredCamera>();
	private ArrayList<DiscoveredCamera> onvifDeviceList = new ArrayList<DiscoveredCamera>();
	private boolean upnpDone = false;
	private boolean natDone = false;
	private int countDone = 0;
	private String externalIp = "";
	private boolean withThumbnail = false;
	private boolean withDefaults = false;
	public ExecutorService pool;

	/**
	 * Include camera thumbnail in the scanning result or not
	 * 
	 * @param withThumbnail
	 *            true if include camera thumbnail
	 */
	public EvercamDiscover withThumbnail(boolean withThumbnail)
	{
		this.withThumbnail = withThumbnail;
		return this;
	}

	/**
	 * Include camera defaults(username, password,URL) in the scanning result or
	 * not
	 * 
	 * @param withThumbnail
	 *            true if include camera defaults
	 */
	public EvercamDiscover withDefaults(boolean withDefaults)
	{
		this.withDefaults = withDefaults;
		return this;
	}

	/**
	 * The wrapped method to scan for cameras in Android.
	 * 
	 * @param scanRange
	 *            the range of IP addresses to scan
	 * @param routerIp
	 *            gateway/router IP address
	 * @return a list of discovered camera devices
	 * @throws Exception
	 */
	public ArrayList<DiscoveredCamera> discoverAllLinux(ScanRange scanRange) throws Exception
	{
		pool = Executors.newFixedThreadPool(DEFAULT_FIXED_POOL);
		// Request for external IP address
		externalIp = NetworkInfo.getExternalIP();

		if (!pool.isShutdown())
		{
			// ONVIF discovery
			pool.execute(onvifRunnable);
			printLogMessage("Discovering ONVIF devices......");
			// Start UPnP discovery
			pool.execute(upnpRunnable);
			printLogMessage("Discovering UPnP devices......");
			// Start UPnP router discovery
			pool.execute(new NatRunnable(scanRange.getRouterIpString())
			{
				@Override
				public void onFinished(ArrayList<NatMapEntry> mapEntries)
				{
					printLogMessage("NAT discovery finished.");
					if (mapEntries != null)
					{
						EvercamDiscover.this.mapEntries = mapEntries;
					}
					natDone = true;
				}
			});
			printLogMessage("Discovering NAT table......");
		}
		
		// Scan to get a list of active IP addresses.
		IpScan ipScan = new IpScan(new ScanResult()
		{
			@Override
			public void onActiveIp(String ip)
			{
				printLogMessage("Active IP: " + ip);
				activeIpList.add(ip);
			}

			@Override
			public void onIpScanned(String ip)
			{
				// TODO Auto-generated method stub
			}
		});
		ipScan.scanAll(scanRange);

		while (!upnpDone || !natDone)
		{
			printLogMessage("Waiting for UPnP & NAT discovery...");
			Thread.sleep(500);
		}

		printLogMessage("Identifying cameras......");
		// For each active IP, request for MAC address and vendor
		for (int index = 0; index < activeIpList.size(); index++)
		{
			if (!pool.isShutdown())
			{
				pool.execute(new IdentifyCameraRunnable(activeIpList.get(index))
				{
					@Override
					public void onCameraFound(DiscoveredCamera discoveredCamera, Vendor vendor)
					{
						try
						{
							discoveredCamera.setExternalIp(externalIp);

							// Add details discovered from UPnP to camera object
							discoveredCamera = mergeUpnpDevicesToCamera(discoveredCamera,
									deviceList);

							// Add details in discovered NAT table(mainly
							// forwarded ports)
							discoveredCamera = mergeNatTableToCamera(discoveredCamera, mapEntries);

							if (withThumbnail)
							{
								printLogMessage("Retrieving thumbnail URL for camera " + discoveredCamera.getIP());
								discoveredCamera.setThumbnail(EvercamQuery.getThumbnailUrlFor(
										discoveredCamera.getVendor(), discoveredCamera.getModel()));
							}

							if (withDefaults)
							{
								printLogMessage("Retrieving defaults for camera " + discoveredCamera.getIP());
								Defaults defaults = vendor.getDefaultModel().getDefaults();
								String username = defaults.getAuth(Auth.TYPE_BASIC).getUsername();
								String password = defaults.getAuth(Auth.TYPE_BASIC).getPassword();
								String jpgUrl = defaults.getJpgURL();
								String h264Url = defaults.getH264URL();
								discoveredCamera.setUsername(username);
								discoveredCamera.setPassword(password);
								discoveredCamera.setJpg(jpgUrl);
								discoveredCamera.setH264(h264Url);
							}

							synchronized (cameraList)
							{
								cameraList.add(discoveredCamera);
							}
						}
						catch (EvercamException e)
						{
							e.printStackTrace();
						}
					}

					@Override
					public void onFinished()
					{
						countDone++;
					}
				});
			}
		}

		while (countDone != activeIpList.size())
		{
			printLogMessage("Camera identification is not finished yet, waiting...");
			Thread.sleep(1000);
		}
		
		//Merge ONVIF devices to discovered camera list
		mergeOnvifDeviceListToCameraList();
		
		pool.shutdown();

		try
		{
			if (!pool.awaitTermination(3600, TimeUnit.SECONDS))
			{
				pool.shutdownNow();
			}
		}
		catch (InterruptedException e)
		{
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}

		return cameraList;
	}

	public static DiscoveredCamera mergeSingleUpnpDeviceToCamera(UpnpDevice upnpDevice,
			DiscoveredCamera discoveredCamera)
	{
		int port = upnpDevice.getPort();
		String model = upnpDevice.getModel();
		if (port > 0)
		{
			discoveredCamera.setHttp(port);
		}
		discoveredCamera.setName(upnpDevice.getFriendlyName());
		discoveredCamera.setModel(model);
		return discoveredCamera;
	}

	public static DiscoveredCamera mergeUpnpDevicesToCamera(DiscoveredCamera camera,
			ArrayList<UpnpDevice> upnpDeviceList)
	{
		try
		{
			if (upnpDeviceList.size() > 0)
			{
				for (UpnpDevice upnpDevice : upnpDeviceList)
				{
					// If IP address matches
					String ipFromUpnp = upnpDevice.getIp();
					if (ipFromUpnp != null && !ipFromUpnp.isEmpty())
					{
						if (camera.getIP().equals(ipFromUpnp))
						{
							mergeSingleUpnpDeviceToCamera(upnpDevice, camera);
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			printLogMessage("Exception while merging UPnP device: " +e.getStackTrace().toString());
		}
		return camera;
	}

	public static DiscoveredCamera mergeNatEntryToCameraIfMatches(DiscoveredCamera camera,
			NatMapEntry mapEntry)
	{
		String natIp = mapEntry.getIpAddress();
		int natInternalPort = mapEntry.getInternalPort();
		int natExternalPort = mapEntry.getExternalPort();

		if (camera.getIP().equals(natIp))
		{
			if (camera.getHttp() == natInternalPort)
			{
				camera.setExthttp(natExternalPort);
			}
			if (camera.getRtsp() == natInternalPort)
			{
				camera.setExtrtsp(natExternalPort);
			}
		}
		return camera;
	}

	public static DiscoveredCamera mergeNatTableToCamera(DiscoveredCamera camera,
			ArrayList<NatMapEntry> mapEntries)
	{
		if (mapEntries.size() > 0)
		{
			for (NatMapEntry mapEntry : mapEntries)
			{
				mergeNatEntryToCameraIfMatches(camera, mapEntry);
			}
		}
		return camera;
	}

	private OnvifRunnable onvifRunnable = new OnvifRunnable()
	{
		@Override
		public void onFinished()
		{
			printLogMessage("ONVIF discovery finished.");
		}

		@Override
		public void onDeviceFound(DiscoveredCamera discoveredCamera)
		{
			printLogMessage("Found ONVIF device: " + discoveredCamera.getIP());
			discoveredCamera.setExternalIp(externalIp);
			onvifDeviceList.add(discoveredCamera);
		}
	};
	
	public void mergeOnvifDeviceListToCameraList()
	{
		if(onvifDeviceList.size() > 0)
		{
			for(DiscoveredCamera onvifCamera : onvifDeviceList)
			{
				boolean matched = false;
				for(DiscoveredCamera discoveredCamera : cameraList)
				{
					if(discoveredCamera.getIP().equals(onvifCamera.getIP()))
					{
						matched = true;
						if(onvifCamera.hasModel())
						{
							discoveredCamera.setModel(onvifCamera.getModel());
							discoveredCamera.setHttp(onvifCamera.getHttp());
						}
							
						break;
					}
				}
				
				if(!matched)
				{
					cameraList.add(onvifCamera);
				}
			}
		}
	}

	private UpnpRunnable upnpRunnable = new UpnpRunnable()
	{

		@Override
		public void onFinished(ArrayList<UpnpDevice> upnpDeviceList)
		{
			printLogMessage("UPnP discovery finished.");
			if (upnpDeviceList != null)
			{
				deviceList = upnpDeviceList;
			}
			upnpDone = true;
		}

		@Override
		public void onDeviceFound(UpnpDevice upnpDevice)
		{
			printLogMessage("Found UPnP device: " + upnpDevice.getIp());
		}
	};
	
	/**
	 * Only print the logging message when logging is enabled
	 * 
	 * @param message The logging message to be printed in console
	 */
	public static void printLogMessage(String message)
	{
		if(Constants.ENABLE_LOGGING)
		{
			System.out.println(message);
		}
	}
}
