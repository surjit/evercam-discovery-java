package io.evercam.network.onvif;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class EnvelopeProbeMatches extends Envelope
{

	@Element(name = "Body")
	public BodyProbeMatches BodyProbeMatches;
}
