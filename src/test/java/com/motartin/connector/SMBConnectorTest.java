package com.motartin.connector;

import com.motartin.utils.PropertyReader;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SMBConnectorTest {
	@Test
	void connector_shouldHaveDefaultValues_ifNoneProvided() {
		final SMBConnector smbConnector = new SMBConnector();
		assertEquals("defaultURL/", smbConnector.getUrl());
		assertEquals("defaultDomain", smbConnector.getContext().getCredentials().getUserDomain());
	}

	@Test
	void connector_shouldUseProperties_ifProvided() {
		final Properties properties = PropertyReader.readConfig("testconfig.properties");
		System.setProperties(properties);
		final SMBConnector smbConnector = new SMBConnector();
		assertEquals("theurlfortesting/", smbConnector.getUrl());
		assertEquals("thedomainfortesting", smbConnector.getContext().getCredentials().getUserDomain());
	}
}
