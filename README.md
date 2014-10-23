#Network Scan

A network scanning library in Java, includes IP scan, port scan, UPnP device discovery, read NAT table, retrieving camera thumbnail and defaults(username, password, urls).

##Usage
See [EvercamConnect](https://github.com/evercam/android.connect)

##Examples
```Java
import io.evercam.network.*;

//Discover all cameras (For Android applications only)
ScanRange scanRange = new ScanRange("192.168.1.1", //router IP
                                    "255.255.255.0"); //subnet mask
ArrayList<DiscoveredCameras> cameraList = EvercamDiscover.discoverAllAndroid(scanRange);

//Discover all active IP address in local network
IpScan ipScan = new IpScan(new ScanResult(){
	@Override
	public void onActiveIp(String ip)
	{
		//Do what you want to the active IP address
	}
});
ipScan.scanAll(scanRange);

//
```
