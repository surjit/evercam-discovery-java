package io.evercam.network;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.NetworkInfo;
import io.evercam.network.discovery.ScanRange;

import java.util.ArrayList;

public class Main
{
	public static void main(String[] args)
	{
		// InputStreamReader inputStream = new InputStreamReader(System.in);
		// BufferedReader keyboardInput = new BufferedReader(inputStream);
		//
		// String routerIp = "", subnetMask = "";
		// try
		// {
		// System.out.println("Please enter router IP: eg. 10.0.0.1");
		// routerIp = keyboardInput.readLine();
		//
		// System.out.println("Please enter subnet mask: eg. 255.255.255.0");
		// subnetMask = keyboardInput.readLine();
		// }
		// catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		String routerIp = NetworkInfo.getLinuxRouterIp();
		String subnetMask = NetworkInfo.getLinuxSubnetMask();

		System.out.println("Network router IP: " + routerIp + " subnet mask: " + subnetMask
				+ "......");
		System.out.println("Scanning...");

		try
		{
			ScanRange scanRange = new ScanRange(routerIp, subnetMask);

			ArrayList<DiscoveredCamera> cameraList = new EvercamDiscover().withDefaults(true)
					.withThumbnail(true).discoverAllAndroid(scanRange);

			System.out.println("Scanning finished, found " + cameraList.size() + " cameras");
			if (cameraList.size() > 0)
			{
				for (DiscoveredCamera camera : cameraList)
				{
					System.out.println(camera.toString());
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
