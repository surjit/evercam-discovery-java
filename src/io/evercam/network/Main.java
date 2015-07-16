package io.evercam.network;

import io.evercam.network.discovery.DiscoveredCamera;
import io.evercam.network.discovery.ScanRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		InputStreamReader inputStream = new InputStreamReader(System.in);
		BufferedReader keyboardInput = new BufferedReader(inputStream);
		
//		String routerIp = "", subnetMask = "";
//		try
//		{
//			System.out.println("Please enter router IP: eg. 10.0.0.1");
//			routerIp = keyboardInput.readLine();
//			
//			System.out.println("Please enter subnet mask: eg. 255.255.255.0");
//			subnetMask = keyboardInput.readLine();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		//System.out.println("Scanning network with router IP: " + routerIp + " subnet mask: " + subnetMask + "......");
		System.out.println("Scanning...");
		
		try
		{
			//ScanRange scanRange = new ScanRange(routerIp, subnetMask);
			ScanRange scanRange = new ScanRange("10.0.0.1", "255.255.255.0");
			
			ArrayList<DiscoveredCamera> cameraList = new EvercamDiscover().discoverAllAndroid(scanRange);
			
			System.out.println("Scanning finished, found " + cameraList.size() + " cameras");
			if(cameraList.size() > 0)
			{
				for(DiscoveredCamera camera: cameraList)
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
