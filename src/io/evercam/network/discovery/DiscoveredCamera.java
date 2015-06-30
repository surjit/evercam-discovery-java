package io.evercam.network.discovery;

import io.evercam.network.Constants;

import java.io.Serializable;
import java.util.Locale;

/**
 * The serializable camera object returned from discovery.
 */
public class DiscoveredCamera implements Serializable
{
	private static final long serialVersionUID = 2100241933714349000L;
	private Integer id;
	private String ip = "";
	private String name = "";
	private String externalIp = "";
	private String mac = "";
	private String vendor = "";
	private String model = "";
	private int upnp = 0; // 1: yes 0:no
	private int bonjour = 0;
	private int onvif = 0;
	private int http = 0;
	private int rtsp = 0;
	private int ftp = 0;
	private int ssh = 0;
	private int https = 0;
	private int exthttp = 0;
	private int extrtsp = 0;
	private int extftp = 0;
	private int extssh = 0;
	private int exthttps = 0;
	private int flag = Constants.TYPE_OTHERS; // 1: camera, 2: router, 3:other
	private String ssid = "";
	private String firstSeen;
	private String lastSeen;
	private String username;
	private String password;
	private String jpg;
	private String h264;
	private int portForwarded = 0; // 1:yes 0:no
	private int evercamConnected = 0; // 1:yes 0:no
	private int active = 0; // 1:yes 0:no
	private String thumbnail = "";

	public DiscoveredCamera(String ip)
	{
		this.ip = ip;
	}

	public String getIP()
	{
		return ip;
	}
	
	public String getName()
	{
		return name;
	}

	public String getExternalIp()
	{
		return externalIp;
	}

	public String getVendor()
	{
		return vendor;
	}

	public void setIP(String ip)
	{
		this.ip = ip;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public void setExternalIp(String externalIp)
	{
		this.externalIp = externalIp;
	}

	public String getMAC()
	{
		return mac;
	}

	public void setMAC(String mac)
	{
		this.mac = mac;
	}

	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

//	public int getUpnp()
//	{
//		return upnp;
//	}
//
//	public void setUpnp(int upnp)
//	{
//		this.upnp = upnp;
//	}
//
//	public int getBonjour()
//	{
//		return bonjour;
//	}
//
//	public void setBonjour(int bonjour)
//	{
//		this.bonjour = bonjour;
//	}
//
//	public int getOnvif()
//	{
//		return onvif;
//	}
//
//	public void setOnvif(int onvif)
//	{
//		this.onvif = onvif;
//	}

	public int getHttp()
	{
		return http;
	}

	public void setHttp(int http)
	{
		this.http = http;
	}

	public int getRtsp()
	{
		return rtsp;
	}

	public void setRtsp(int rtsp)
	{
		this.rtsp = rtsp;
	}

//	public int getFtp()
//	{
//		return ftp;
//	}
//
//	public void setFtp(int ftp)
//	{
//		this.ftp = ftp;
//	}
//
//	public int getSsh()
//	{
//		return ssh;
//	}
//
//	public void setSsh(int ssh)
//	{
//		this.ssh = ssh;
//	}

	public int getExthttp()
	{
		return exthttp;
	}

	public void setExthttp(int exthttp)
	{
		this.exthttp = exthttp;
	}

	public int getExtrtsp()
	{
		return extrtsp;
	}

	public void setExtrtsp(int extrtsp)
	{
		this.extrtsp = extrtsp;
	}

//	public int getExtftp()
//	{
//		return extftp;
//	}
//
//	public void setExtftp(int extftp)
//	{
//		this.extftp = extftp;
//	}
//
//	public int getExtssh()
//	{
//		return extssh;
//	}
//
//	public void setExtssh(int extssh)
//	{
//		this.extssh = extssh;
//	}
//
//	public void setFlag(int flag)
//	{
//		this.flag = flag;
//	}
//
//	public int getFlag()
//	{
//		return flag;
//	}

//	public String getSsid()
//	{
//		return ssid;
//	}
//
//	public void setSsid(String ssid)
//	{
//		this.ssid = ssid;
//	}
//
//	public String getFirstSeen()
//	{
//		return firstSeen;
//	}
//
//	public void setFirstSeen(String firstSeen)
//	{
//		this.firstSeen = firstSeen;
//	}
//
//	public String getLastSeen()
//	{
//		return lastSeen;
//	}
//
//	public void setLastSeen(String lastSeen)
//	{
//		this.lastSeen = lastSeen;
//	}
//
//	public int getActive()
//	{
//		return active;
//	}
//
//	public void setActive(int active)
//	{
//		this.active = active;
//	}
//
//	public boolean isActive()
//	{
//		return getActive() == 1;
//	}

	@Override
	public String toString()
	{
		return "Camera [id=" + id + ", ip=" + ip  + ", name=" + name + ", externalIp=" + externalIp + ", mac=" + mac + ", vendor=" + vendor
				+ ",model=" + model + ",bonjour=" + bonjour + ",upnp=" + upnp + ",onvif=" + onvif
				+ ",http=" + http + ",rtsp=" + rtsp + ",https=" + https + ",ftp=" + ftp + ",ssh="
				+ ssh + ",extrtsp=" + extrtsp + ",exthttp=" + exthttp + ",flag=" + flag
				+ ",firstseen=" + firstSeen + ",lastseen=" + lastSeen + ",username=" + username
				+ ",password=" + password + ",jpg=" + jpg + ",h264=" + h264 + ",ssid=" + ssid
				+ ",active=" + active + "]";
	}

	public int getHttps()
	{
		return https;
	}

	public void setHttps(int https)
	{
		this.https = https;
	}

	public int getExthttps()
	{
		return exthttps;
	}

	public void setExthttps(int exthttps)
	{
		this.exthttps = exthttps;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getJpg()
	{
		return jpg;
	}

	public String getH264()
	{
		return h264;
	}

	public void setJpg(String jpg)
	{
		this.jpg = jpg;
	}

	public void setH264(String h264)
	{
		this.h264 = h264;
	}

//	public int getPortForwarded()
//	{
//		return portForwarded;
//	}
//
//	public void setPortForwarded(int portForwarded)
//	{
//		this.portForwarded = portForwarded;
//	}
//
//	public int getEvercamConnected()
//	{
//		return evercamConnected;
//	}

	public String getThumbnail() 
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) 
	{
		this.thumbnail = thumbnail;
	}

//	public void setEvercamConnected(int evercamConnected)
//	{
//		this.evercamConnected = evercamConnected;
//	}

	public boolean hasHTTP()
	{
		if (getHttp() > 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasRTSP()
	{
		if (getRtsp() != 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasHTTPS()
	{
		if (getHttps() != 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasExternalHttp()
	{
		if (getExthttp() != 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasExternalRtsp()
	{
		if (getExtrtsp() != 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasExternalHttps()
	{
		if (getExthttps() != 0)
		{
			return true;
		}
		return false;
	}

	public boolean hasModel()
	{
		if (getModel() != null && !getModel().equals(""))
		{
			return true;
		}
		return false;
	}

	public boolean hasMac()
	{
		if (getMAC() != null && !getMAC().equals("") && !getMAC().equals(Constants.EMPTY_MAC))
		{
			return true;
		}
		return false;
	}

	public boolean hasVendor()
	{
		if (getVendor() != null && !getVendor().equals(""))
		{
			return true;
		}
		return false;
	}

	public boolean hasUsername()
	{
		if (getUsername() != null && !getUsername().isEmpty())
		{
			return true;
		}
		return false;
	}

	public boolean hasPassword()
	{
		if (getPassword() != null && !getPassword().isEmpty())
		{
			return true;
		}
		return false;
	}

	public boolean hasJpgURL()
	{
		if (getJpg() != null && !getJpg().isEmpty() && getJpg() != "/")
		{
			return true;
		}
		return false;
	}

	public boolean hasH264URL()
	{
		if (getH264() != null && !getH264().isEmpty() && getH264() != "/")
		{
			return true;
		}
		return false;
	}

	public boolean modelContainsVendorName()
	{
		if (getModel().toUpperCase(Locale.UK).contains(getVendor().toUpperCase(Locale.UK)))
		{
			return true;
		}
		return false;
	}
	
	public boolean hasExternalIp()
	{
		if(getExternalIp() != null && !getExternalIp().isEmpty())
		{
			return true;
		}
		return false;
	}
	
	public boolean hasName()
	{
		return !getName().isEmpty();
	}
	
	public DiscoveredCamera merge(DiscoveredCamera discoveredCamera)
	{
        if(discoveredCamera.hasMac())
        {
            setMAC(discoveredCamera.getMAC());
        }
        if(discoveredCamera.hasRTSP())
        {
            setRtsp(discoveredCamera.getRtsp());
        }
        if(!hasVendor() && discoveredCamera.hasVendor())
        {
            setVendor(discoveredCamera.getVendor());
        }
        if(!hasHTTP() && discoveredCamera.hasHTTP())
        {
            setHttp(discoveredCamera.getHttp());
        }
        if(!hasModel() && discoveredCamera.hasModel())
        {
            setModel(discoveredCamera.getModel());
        }
        if(discoveredCamera.hasExternalHttp())
        {
            setExthttp(discoveredCamera.getExthttp());
        }
        if(discoveredCamera.hasExternalRtsp())
        {
            setExtrtsp(discoveredCamera.getExtrtsp());
        }
        if(discoveredCamera.hasName())
        {
        	setName(discoveredCamera.getName());
        }
        
        return this;
	}
}
