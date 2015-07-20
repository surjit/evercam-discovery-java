package io.evercam.network.onvif;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/04/discovery")
@Root(strict = false)
public class ProbeMatches
{

	@ElementList(entry = "ProbeMatch", inline = true)
	public List<ProbeMatch> listProbeMatches;
}
