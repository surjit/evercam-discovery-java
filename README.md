#Network Scan

A network scanning library in Java, includes IP scan, port scan, UPnP device discovery, read NAT table, retrieving camera thumbnail and defaults(username, password, urls).

##Usage
See [EvercamConnect](https://github.com/evercam/android.connect)

##Examples

Scan local network, find camera devices with options of including camera thumbnail and defaults or not, and return all discovered camera details(IP address, internal & external ports, MAC address, vendor, model, thumbmnail, default username, password and URLs).
```Java
import io.evercam.network.*;

EvercamDiscover evercamDiscover = new EvercamDiscover()
				.withThumbnail(true) //Include thumbnail URL or not
				.withDefaults(true); //Include camera defaults or not

//Discover all cameras
ScanRange scanRange = new ScanRange("192.168.1.1", //router IP
                                    "255.255.255.0"); //subnet mask
ArrayList<DiscoveredCameras> cameraList = evercamDiscover.discoverAllAndroid(scanRange);
```
Alternatively, you can scan for specific details seperately:
```Java
//Discover all active IP address in local network
IpScan ipScan = new IpScan(new ScanResult(){
	@Override
	public void onActiveIp(String ip)
	{
		//Do what you want to the active IP address
	}
});
ipScan.scanAll(scanRange);

//Discover UPnP devices in local network with device callback
UpnpDiscovery upnpDiscovery = new UpnpDiscovery(new UpnpResult(){
	@Override
	public void onUpnpDeviceFound(UpnpDevice upnpDevice)
	{
		//Do what you want to the UPnP device
	});
upnpDiscovery.discoverAll();
ArrayList<UpnpDevice> deviceList = upnpDiscovery.getUpnpDevices();

//Retrieve NAT table
GatewayDevice gatewayDevice = new GatewayDevice("192.168.1.1");
ArrayList<NatMapEntry> mapEntries = gatewayDevice.getNatTableArray();

//Camera manufacturer defaults
Defaults defaults = new Vendor("hikvision").getDefaultModel().getDefaults();

//Camera thumbnail URL
String thumbnailUrl = CambaseAPI.getThumbnailUrlFor("hikvision", "ds-2cd7164-e")
```
