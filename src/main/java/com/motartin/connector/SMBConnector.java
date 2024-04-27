package com.motartin.connector;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.Configuration;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.motartin.application.Constants.PropertyKey.*;

/**
 * Connector that connects to an SMB share specified in the properties file.
 */
@Slf4j
public final class SMBConnector implements Connector {

	@Getter(AccessLevel.PACKAGE)
	private final String url;
	@Getter(AccessLevel.PACKAGE)
	private CIFSContext context;

	public SMBConnector() {
		url = System.getProperty(SMB_URL, "defaultURL") + "/";

		try {
			BaseContext baseCxt = new BaseContext(getConfiguration());
			context = baseCxt.withCredentials(getNtlmPasswordAuthenticator());
		} catch (CIFSException e) {
			log.debug("Could not init SMB connector.", e);
		}
	}

	private static NtlmPasswordAuthenticator getNtlmPasswordAuthenticator() {
		return new NtlmPasswordAuthenticator(
				System.getProperty(SMB_DOMAIN, "defaultDomain"),
				System.getProperty(SMB_USER, "defaultUserName"),
				System.getProperty(SMB_PASSWORD, "defaultPassword"));
	}

	private static Configuration getConfiguration() throws CIFSException {
		Properties jcifsProperties  = new Properties();
		jcifsProperties.setProperty("jcifs.smb.client.enableSMB2", "true");
		jcifsProperties.setProperty("jcifs.smb.client.dfs.disabled","true");
		return new PropertyConfiguration(jcifsProperties);
	}

	@Override
	public synchronized InputStream getFile(String fileName) {
		try (SmbFile file = new SmbFile(url + fileName, context)) {
			return file.getInputStream();
		}
		catch (IOException e) {
			log.debug("Could not get file", e);
			return null;
		}
	}

	@Override
	public List<String> listFiles() {
		List<String> result = new ArrayList<>();
		try {
			try (final SmbFile smb = new SmbFile(url, context)) {
				return Arrays.stream(smb.listFiles())
						.filter(file -> {
							try {
								return !file.isDirectory();
							} catch (SmbException e) {
								log.debug("Could not work on file", e);
								throw new RuntimeException();
							}
						})
						.map(smbFile -> {
							String fileName = smbFile.getName();
							smbFile.close();
							return fileName;
						})
						.filter(ImageTypePredicate.INSTANCE)
						.toList();
			}
		}
		catch (MalformedURLException | SmbException e) {
			log.debug(e.getMessage());
		}
		return result;
	}
}
