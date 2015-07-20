package io.evercam.network.onvif;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
@Root(strict = false)
public class EndpointReference
{
	@Element
	public String Address;
}
