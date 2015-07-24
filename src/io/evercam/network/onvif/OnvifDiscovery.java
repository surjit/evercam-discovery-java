package io.evercam.network.onvif;

import io.evercam.network.EvercamDiscover;
import io.evercam.network.discovery.DiscoveredCamera;

import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeBuilder;

public abstract class OnvifDiscovery
{
	private static final int SOCKET_TIMEOUT = 4000;
	private static final String PROBE_MESSAGE = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"><s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</a:Action><a:MessageID>uuid:21859bf9-6193-4c8a-ad50-d082e6d296ab</a:MessageID><a:ReplyTo><a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address></a:ReplyTo><a:To s:mustUnderstand=\"1\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</a:To></s:Header><s:Body><Probe xmlns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\"><d:Types xmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:dp0=\"http://www.onvif.org/ver10/network/wsdl\">dp0:NetworkVideoTransmitter</d:Types></Probe></s:Body></s:Envelope>";
	private static final String PROBE_IP = "239.255.255.250";
	private static final int PROBE_PORT = 3702;
	private static final String SCOPE_NAME = "onvif://www.onvif.org/name/";
	private static final String SCOPE_HARDWARE = "onvif://www.onvif.org/hardware/";

	public OnvifDiscovery()
	{

	}

	public ArrayList<DiscoveredCamera> probe()
	{
		ArrayList<DiscoveredCamera> cameraList = new ArrayList<DiscoveredCamera>();

		try
		{
			DatagramSocket datagramSocket = new DatagramSocket();
			datagramSocket.setSoTimeout(SOCKET_TIMEOUT);
			InetAddress multicastAddress = InetAddress.getByName(PROBE_IP);

			if (multicastAddress == null)
			{
				//System.out.println("InetAddress.getByName() for multicast returns null");
				return cameraList;
			}

			// Send the UDP probe message
			String soapMessage = getProbeSoapMessage();
			// System.out.println(soapMessage);
			byte[] soapMessageByteArray = soapMessage.getBytes();
			DatagramPacket datagramPacketSend = new DatagramPacket(soapMessageByteArray,
					soapMessageByteArray.length, multicastAddress, PROBE_PORT);
			datagramSocket.send(datagramPacketSend);

			ArrayList<String> uuidArrayList = new ArrayList<String>();
			while (true)
			{
				// System.out.println("Receiving...");
				byte[] responseMessageByteArray = new byte[3072];
				DatagramPacket datagramPacketRecieve = new DatagramPacket(responseMessageByteArray,
						responseMessageByteArray.length);
				datagramSocket.receive(datagramPacketRecieve);

				String responseMessage = new String(datagramPacketRecieve.getData());

				//System.out.println("\nResponse Message:\n" + responseMessage);

				StringReader stringReader = new StringReader(responseMessage);
				InputNode localInputNode = NodeBuilder.read(stringReader);
				EnvelopeProbeMatches localEnvelopeProbeMatches = new Persister().read(
						EnvelopeProbeMatches.class, localInputNode);
				if (localEnvelopeProbeMatches.BodyProbeMatches.ProbeMatches.listProbeMatches.size() <= 0)
				{
					//System.out.println("No probe matches");
					continue;
				}

				ProbeMatch localProbeMatch = localEnvelopeProbeMatches.BodyProbeMatches.ProbeMatches.listProbeMatches
						.get(0);
				// System.out.println("Probe matches with UUID:\n" +
				// localProbeMatch.EndpointReference.Address + " URL: " +
				// localProbeMatch.XAddrs);
				if (uuidArrayList.contains(localProbeMatch.EndpointReference.Address))
				{
					EvercamDiscover.printLogMessage("ONVIFDiscovery: Address " + localProbeMatch.EndpointReference.Address
							+ " already added");
					continue;
				}
				uuidArrayList.add(localProbeMatch.EndpointReference.Address);
				DiscoveredCamera discoveredCamera = getCameraFromProbeMatch(localProbeMatch);
				onActiveOnvifDevice(discoveredCamera);
				cameraList.add(discoveredCamera);
			}
		}
		catch (Exception e)
		{
			// ONVIF timeout. Don't print anything.
		}

		return cameraList;
	}

	private static String getProbeSoapMessage()
	{
		return PROBE_MESSAGE.replaceFirst("<a:MessageID>uuid:.+?</a:MessageID>",
				"<a:MessageID>uuid:" + UUID.randomUUID().toString() + "</a:MessageID>");
	}

	private static DiscoveredCamera getCameraFromProbeMatch(ProbeMatch probeMatch)
	{
		DiscoveredCamera discoveredCamera = null;
		try
		{
			String[] urlArray = probeMatch.XAddrs.split("\\s");
			String[] scopeArray = probeMatch.Scopes.split("\\s");
			String scopeName = "";
			String scopeHardware = "";
			for (String scope : scopeArray)
			{
				if (scope.contains(SCOPE_NAME))
				{
					final String URL_SPACE = "%20";
					scopeName = scope.replace(SCOPE_NAME, "").replace(URL_SPACE, " ");
				}
				if (scope.contains(SCOPE_HARDWARE))
				{
					final String URL_SPACE = "%20";
					scopeHardware = scope.replace(SCOPE_HARDWARE, "").replace(URL_SPACE, " ");
				}
			}

			try
			{
				URL localURL = new URL(urlArray[(-1 + urlArray.length)]);
				discoveredCamera = new DiscoveredCamera(localURL.getHost());
				int httpPort = localURL.getPort();
				if (httpPort == -1)
				{
					httpPort = 80;
				}
				discoveredCamera.setHttp(httpPort);
				if (!scopeName.isEmpty())
				{
					discoveredCamera.setVendor(scopeName.toLowerCase(Locale.UK));
				}
				if (!scopeHardware.isEmpty())
				{
					discoveredCamera.setModel(scopeHardware.toLowerCase(Locale.UK));
				}
			}
			catch (MalformedURLException localMalformedURLException)
			{
				EvercamDiscover.printLogMessage("Cannot parse xAddr: " + probeMatch.XAddrs);
			}
		}
		catch (Exception e)
		{
			EvercamDiscover.printLogMessage("Parse ONVIF search result error: " + e.getMessage());
		}
		return discoveredCamera;
	}

	public abstract void onActiveOnvifDevice(DiscoveredCamera discoveredCamera);
}
