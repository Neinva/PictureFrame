package com.motartin.application;

public class Constants {
	public static class App {
		public static String APPLICATION_NAME = "PictureFrame";
		public static String FALLBACK_PROPERTY_FILE_NAME = "defaultconfig.properties";
		public static String STANDARD_PROPERTY_FILE_NAME = "pictureframeconfig.properties";
		public static String IMAGE_REFRESH_TIMEOUT_ARG = "image.refresh.timeout";
		public static String IMAGE_REFRESH_TIMEOUT_DEFAULT_IN_SECONDS = "120";
		public static String DEFAULT_IMAGE_TYPES = "jpg,jpeg";
	}

	/**
	 * All keys that can be set in the property file.
	 */
	public static class PropertyKey {
		public static String IMAGE_TYPES = "image.types";
	}

	private Constants() {
		throw new AssertionError(getClass().getSimpleName() + " cannot be instantiated.");
	}
}
