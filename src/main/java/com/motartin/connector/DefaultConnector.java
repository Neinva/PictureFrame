package com.motartin.connector;

import com.motartin.application.PictureFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default implementation of the Connector interface.
 * Without further configuration specified in the config properties it will:
 * list all ".jpg" and ".jpeg" files in the same folder as the .jar.
 */
@Slf4j
public class DefaultConnector implements Connector {

	private List<String> files;

	@Override
	public InputStream getFile(String fileName) {
		try {
			log.debug("Getting file " + fileName);
			return Files.newInputStream(Paths.get(PictureFrame.getAppLocationPath(), fileName));
		} catch (IOException e) {
			log.error("Could not read " + fileName, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> listFiles() {
		try (Stream<Path> stream = Files.list(Path.of(PictureFrame.getAppLocationPath()))) {
			files = stream
					.filter(file -> !Files.isDirectory(file))
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(ImageTypePredicate.INSTANCE)
					.collect(Collectors.toList());
		} catch (IOException e) {
			log.error("Could not read files {}", e.getMessage());
		}
		return files;
	}
}
