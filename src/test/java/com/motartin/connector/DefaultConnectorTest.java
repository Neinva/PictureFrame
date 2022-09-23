package com.motartin.connector;

import com.motartin.application.PictureFrame;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class DefaultConnectorTest {
	@TempDir
	static Path tempDir;

	@BeforeAll
	static void setupTestEnv() {
		final MockedStatic<PictureFrame> picViewerAppMockedStatic = mockStatic(PictureFrame.class);
		picViewerAppMockedStatic.when(PictureFrame::getAppLocationPath).thenReturn(tempDir.toFile().getAbsolutePath());
	}

	@BeforeEach
	void prepareTest() {
		try {
			Files.write(tempDir.resolve("first.jpeg"), new byte[0]);
			Files.write(tempDir.resolve("2.jpeg"), new byte[0]);
			Files.write(tempDir.resolve("test.someweirdending"), new byte[0]);
			Files.write(tempDir.resolve("first.jpg"), new byte[0]);
			Files.write(tempDir.resolve("test.txt"), new byte[0]);
			Files.write(tempDir.resolve("first.png"), new byte[0]);
			Files.write(tempDir.resolve("first.orf"), new byte[0]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void listFiles_shouldReturnListOnlyContainingImages() {
		final DefaultConnector defaultConnector = new DefaultConnector();
		assertEquals(tempDir.toString(), PictureFrame.getAppLocationPath());
		assertEquals(3, defaultConnector.listFiles().size());
		assertTrue(defaultConnector.listFiles().contains("first.jpeg"));
	}

	@Test
	void listFiles_shouldFind_newlyAddedFiles() throws IOException {
		final DefaultConnector defaultConnector = new DefaultConnector();
		Files.write(tempDir.resolve("another.jpg"), new byte[0]);
		assertEquals(4, defaultConnector.listFiles().size());
		Files.delete(tempDir.resolve("another.jpg"));
	}

	@Test
	void listFiles_shouldReturnEmptyList_whenNoPicturesPresent() throws IOException {
		final DefaultConnector defaultConnector = new DefaultConnector();
		assertEquals(3, defaultConnector.listFiles().size());
		Files.delete(tempDir.resolve("first.jpeg"));
		Files.delete(tempDir.resolve("2.jpeg"));
		Files.delete(tempDir.resolve("first.jpg"));
		assertEquals(0, defaultConnector.listFiles().size());
	}

	@Test
	void listFiles_respectsConfigParametersForFileType() {
		System.setProperty("image.types", "png,orf");
		final DefaultConnector defaultConnector = new DefaultConnector();
		assertEquals(2, defaultConnector.listFiles().size());
		System.clearProperty("image.types");
	}
}
