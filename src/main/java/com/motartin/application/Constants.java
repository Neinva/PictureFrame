package com.motartin.application;

public class Constants {

	/**
	 * General purpose constants of the app
	 */
	public static class App {
		public static String APPLICATION_NAME = "PictureFrame";
		public static String FALLBACK_PROPERTY_FILE_NAME = "defaultconfig.properties";
		public static String STANDARD_PROPERTY_FILE_NAME = "pictureframeconfig.properties";
		public static String IMAGE_REFRESH_TIMEOUT_DEFAULT_IN_SECONDS = "60";
		public static String DEFAULT_IMAGE_TYPES = "jpg,jpeg";
	}

	/**
	 * Keys that can be set in the property file.
	 */
	public static class PropertyKey {
		public static String CONNECTOR_TYPE = "connector.type";
		public static String SMB_URL="smb.url";
		public static String SMB_DOMAIN="smb.domain";
		public static String SMB_USER="smb.user";
		public static String SMB_PASSWORD="smb.password";
		public static String IMAGE_TYPES = "image.types";
		public static String IMAGE_REFRESH_TIMEOUT = "image.refresh.timeout";
	}

	private Constants() {
		throw new AssertionError(getClass().getSimpleName() + " cannot be instantiated.");
	}
}
