package io.evercam.network.onvif;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery")
@Root(strict = false)
public class ProbeMatch
{

	@Element(required = false)
	@Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
	public EndpointReference EndpointReference;

	@Element(required = false)
	public String MetadataVersion;

	@Element
	public String Scopes;

	@Element(required = false)
	public String Types;

	@Element
	public String XAddrs;
}
