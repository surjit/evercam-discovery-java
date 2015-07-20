package io.evercam.network;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

public class EvercamDiscover
{
	public static final int DEFAULT_FIXED_POOL = 20;
	
	private ArrayList<String> activeIpList = new ArrayList<String>();
	private ArrayList<UpnpDevice> deviceList = new ArrayList<UpnpDevice>();//UPnP device list
	private ArrayList<NatMapEntry> mapEntries = new ArrayList<NatMapEntry>();//NAT table
	private ArrayList<DiscoveredCamera> cameraList = new ArrayList<DiscoveredCamera>();
	private boolean upnpDone = false;
	private boolean natDone = false;
	private int countDone = 0;
	private String externalIp = "";
	private boolean withThumbnail = false;
	private boolean withDefaults = false;
	public ExecutorService pool;
	
	/**
	 * Include camera thumbnail in the scanning result or not
	 * @param withThumbnail true if include camera thumbnail
	 */
	public EvercamDiscover withThumbnail(boolean withThumbnail)
	{
		this.withThumbnail = withThumbnail;
		return this;
	}
	
	/**
	 * Include camera defaults(username, password,URL) in the scanning result or not
	 * @param withThumbnail true if include camera defaults
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
	public ArrayList<DiscoveredCamera> discoverAllAndroid(ScanRange scanRange) throws Exception
	{
		pool = Executors.newFixedThreadPool(DEFAULT_FIXED_POOL);
		// Request for external IP address
		externalIp = NetworkInfo.getExternalIP();
		// Scan to get a list of active IP addresses.
		IpScan ipScan = new IpScan(new ScanResult(){
			@Override
			public void onActiveIp(String ip)
			{
				System.out.println("Active IP: " + ip);
				activeIpList.add(ip);
			}

			@Override
			public void onIpScanned(String ip) {
				// TODO Auto-generated method stub
			}
			
		});
		ipScan.scanAll(scanRange);

		if(!pool.isShutdown())
		{
			// ONVIF discovery
			pool.execute(onvifRunnable);
			// Start UPnP discovery
			pool.execute(upnpRunnable);
			// Start UPnP router discovery
			pool.execute(new NatRunnable(scanRange.getRouterIpString())
			{

				@Override
				public void onFinished(ArrayList<NatMapEntry> mapEntries)
				{
					if(mapEntries != null)
					{
						EvercamDiscover.this.mapEntries = mapEntries;
					}
					natDone = true;
				}
			});
		}
		
		while(!upnpDone || ! natDone)
		{
			Thread.sleep(500);
		}
		
		// For each active IP, request for MAC address and vendor
		for (int index = 0; index < activeIpList.size() ; index ++)
		{
			if(!pool.isShutdown())
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
							discoveredCamera = mergeUpnpDevicesToCamera(discoveredCamera, deviceList);
		
							// Add details in discovered NAT table(mainly forwarded ports) 
							discoveredCamera = mergeNatTableToCamera(discoveredCamera, mapEntries);
							
							if(withThumbnail)
							{
								discoveredCamera.setThumbnail(EvercamQuery.getThumbnailUrlFor(discoveredCamera.getVendor(), discoveredCamera.getModel()));
							}
							
							if(withDefaults)
							{
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
							
							synchronized(cameraList)
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
						countDone ++;
					}
				});
			}
		}

		while(countDone != activeIpList.size())
		{
			Thread.sleep(1000);
		}
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
	
    public static DiscoveredCamera mergeSingleUpnpDeviceToCamera(UpnpDevice upnpDevice, DiscoveredCamera discoveredCamera)
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
							mergeSingleUpnpDeviceToCamera(upnpDevice, camera);
							break;
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
	
	public static DiscoveredCamera mergeNatEntryToCameraIfMatches(DiscoveredCamera camera, NatMapEntry mapEntry)
	{
		String natIp = mapEntry.getIpAddress();
		int natInternalPort = mapEntry.getInternalPort();
		int natExternalPort = mapEntry.getExternalPort();
		
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
		return camera;
	}
	
	public static DiscoveredCamera mergeNatTableToCamera(DiscoveredCamera camera, ArrayList<NatMapEntry> mapEntries)
	{
		if(mapEntries.size() > 0)
		{
			for(NatMapEntry mapEntry : mapEntries)
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
			// TODO Auto-generated method stub
		}

		@Override
		public void onDeviceFound(DiscoveredCamera discoveredCamera)
		{
			discoveredCamera.setExternalIp(externalIp);
		}
	};
	
	private UpnpRunnable upnpRunnable = new UpnpRunnable()
	{

		@Override
		public void onFinished(ArrayList<UpnpDevice> upnpDeviceList)
		{
			if(upnpDeviceList  != null)
			{
				deviceList = upnpDeviceList;
			}
			upnpDone = true;
		}

		@Override
		public void onDeviceFound(UpnpDevice upnpDevice)
		{
			// TODO Auto-generated method stub
		}
	};
}
