package com.motartin.connector;

import java.io.InputStream;
import java.util.List;

/**
 * Interface for classes providing access to files.
 */
public interface Connector {
	
	/**
	 * Get access to the file with the provided fileName as InputStream
	 * @param fileName name of the file to be retrieved
	 * @return an ordered stream of bytes representing the file
	 */
	InputStream getFile(String fileName);
	
	/**
	 * @return a {@link List} of {@link String} containing all files the connector is configured to access
	 */
	List<String> listFiles();
}
