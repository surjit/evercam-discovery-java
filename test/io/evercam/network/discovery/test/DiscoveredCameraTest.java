package io.evercam.network.discovery.test;

import static org.junit.Assert.*;
import io.evercam.network.discovery.DiscoveredCamera;

import org.junit.Test;

public class DiscoveredCameraTest
{
	@Test
	public void testToJson()
	{
		String jsonString = new DiscoveredCamera("123").toJsonObject().toString();
		System.out.println(jsonString);
		assertTrue(jsonString.contains("null"));
	}
}
