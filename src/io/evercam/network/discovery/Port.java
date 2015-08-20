package io.evercam.network.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Port
{
	public static final String TYPE_HTTP = "HTTP";
	public static final String TYPE_RTSP = "RTSP";
	
	private int value = 0;
	private String type = "";
	private boolean isActive;
	
	public Port(String type, int value)
	{
		setValue(value);
		setType(type);
	}
	
	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public boolean isActive()
	{
		return isActive;
	}
	public void setIsOpen(boolean isActive)
	{
		this.isActive = isActive;
	}
	
	public boolean isHttp()
	{
		return getType().equals(TYPE_HTTP);
	}
	
	public boolean isRtsp()
	{
		return getType().equals(TYPE_RTSP);
	}
	
	public static boolean isReachable(String ip, int port) throws Exception
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

			}
			catch (Exception e)
			{
				// Timeout reading from the stream
				// System.out.println("Exception read from stream");
				// e.printStackTrace();
				return true;
			}

			return result != -1;
		}
		catch (IOException e)
		{
			// System.out.println("Exception connect socket");
			// e.printStackTrace();
			return false;
		}
	}
}
