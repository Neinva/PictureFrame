package com.motartin.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PropertyReaderTest {
	@Test
	void whenPropertyFileInResourceBundle_thenReaderCanReadIt() {
		final Properties properties = PropertyReader.readConfig("testconfig.properties");
		assertEquals("from the testconfig", properties.getProperty("test.property"));
		assertEquals("thedomainfortesting", properties.getProperty("smb.domain"));
		assertEquals("theurlfortesting", properties.getProperty("smb.url"));
	}

	@Test
	@Disabled
	//TODO - read from simulated root dir instead of copied resources
	void whenPropertyFileInAppRoot_thenReaderCanReadIt() {
		final Properties properties = PropertyReader.readConfig("rootconfig.properties");
		assertEquals("4321", properties.getProperty("test.property"));
		assertEquals("rootdomain", properties.getProperty("smb.domain"));
		assertEquals("rooturl", properties.getProperty("smb.url"));
	}

	@Test
	void whenPropertyFileDoesNotExist_thenReaderWillFallBackToDefault() {
		final Properties properties = PropertyReader.readConfig("notexistingfilename.properties");
		assertEquals("from the default config file", properties.getProperty("test.property"));
	}
}
