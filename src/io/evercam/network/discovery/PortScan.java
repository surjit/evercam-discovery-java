package io.evercam.network.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class PortScan
{
	PortScanResult portScanResult;
	private ArrayList<Integer> activePortList;
	public final int[] STANDARD_PORTS = { 20, 21, 22, 80, 443, 554 };
	public static final int TYPE_COMMON = 1;
	public static final int TYPE_STANDARD = 0;

	public PortScan(PortScanResult portScanResult)
	{
		this.portScanResult = portScanResult;
	}

	public static boolean isPortReachable(String ip, int port) throws Exception
	{
		try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
            socket.setSoTimeout(1000);

            /**
             * Read from the stream to check if data exists in the stream
             * Because in E-Play the socket is connected even for closed ports
             */
            int result;
            try
            {
                InputStream inputStream = socket.getInputStream();
                result = inputStream.read();
                socket.close();

            } catch (Exception e)
            {
                //Timeout reading from the stream
//                System.out.println("Exception read from stream");
//                e.printStackTrace();
                return true;
            }

            return result != -1;
        }
        catch (IOException e)
        {
//        	System.out.println("Exception connect socket");
//            e.printStackTrace();
            return false;
        }
	}

	// scan both stand and common ports
	public void start(String ip) throws Exception
	{
		activePortList = new ArrayList<Integer>();
		scanByStandard(ip, STANDARD_PORTS);
	}

	public void scanByStandard(String ip, int[] ports) throws Exception
	{
		int port;
		for (int i = 0; i < ports.length; i++)
		{
			port = ports[i];
			if (isPortReachable(ip, port))
			{
				//System.out.println("Active port added: " + ip + ":" + port);
				activePortList.add(port);
				if (portScanResult != null)
				{
					portScanResult.onPortActive(port);
				}
			}
		}
	}

	public ArrayList<Integer> getActivePorts() throws EvercamException
	{
		if (activePortList != null)
		{
			return activePortList;
		}
		else
		{
			throw new EvercamException(EvercamException.MSG_PORT_SCAN_NOT_STARTED);
		}
	}
	
	public static DiscoveredCamera mergePort(DiscoveredCamera camera, int port)
	{
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
		
		return camera;
	}

}
