package io.evercam.network;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.NetworkInfo;
import io.evercam.network.discovery.ScanRange;

import java.util.ArrayList;

public class Main
{
	/**
	 * Discover all cameras in local network and print them in console
	 * 
	 * @param args pass parameter -v/--verbose to enable verbose logging
	 */
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
		
		if(args.length > 0)
		{
			if(args[0].equals("-v") || args[0].equals("--verbose"))
			{
				Constants.ENABLE_LOGGING = true;
			}
		}
		
		String routerIp = NetworkInfo.getLinuxRouterIp();
		String subnetMask = NetworkInfo.getLinuxSubnetMask();

		EvercamDiscover.printLogMessage("Network router IP: " + routerIp + " subnet mask: " + subnetMask);
		EvercamDiscover.printLogMessage("Scanning...");

		try
		{
			ScanRange scanRange = new ScanRange(routerIp, subnetMask);

			ArrayList<DiscoveredCamera> cameraList = new EvercamDiscover().withDefaults(true)
					.withThumbnail(true).discoverAllLinux(scanRange);

			EvercamDiscover.printLogMessage("Scanning finished, found " + cameraList.size() + " cameras");
			if (cameraList.size() > 0)
			{
				for (DiscoveredCamera camera : cameraList)
				{
					System.out.println(camera.toString());
				}
			}
			EvercamDiscover.printLogMessage("On normal completion: 0");
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EvercamDiscover.printLogMessage("On error: 1");
			System.exit(1);
		}
	}
}
