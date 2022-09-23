package com.motartin.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileHelperTest {

	@TempDir
	static Path tempDir;

	@Test
	void copyThrowsException_whenNoInputStreamProvided() {
		final NullPointerException nullPointerException = assertThrows(NullPointerException.class, () ->
				FileHelper.copyInputStreamToFile(null, new File(Path.of(tempDir.toString(), "tempFile.tmp").toUri())));
		assertTrue(nullPointerException.getMessage().contains("inputStream") && nullPointerException.getMessage().contains("is null"));
	}

	@Test
	void copyThrowsException_whenNoFileProvided() throws IOException {
		writeToTempFolder("the text", "nofileinput.tmp");
		final NullPointerException nullPointerException = assertThrows(NullPointerException.class, () ->
				FileHelper.copyInputStreamToFile(new FileInputStream(Path.of(tempDir.toString(), "nofileinput.tmp").toFile()), null));
		assertTrue(nullPointerException.getMessage().contains("file") && nullPointerException.getMessage().contains("is null"));
	}

	@Test
	void copyWorks_whenArgumentsAreValid() throws IOException {
		writeToTempFolder("this should appear in the file", "workingExampleInput.tmp");
		final File targetFile = new File(Path.of(tempDir.toString(), "workingResult.tmp").toUri());
		FileHelper.copyInputStreamToFile(
				new FileInputStream(Path.of(tempDir.toString(), "workingExampleInput.tmp").toFile()),
				targetFile).join();
		final String result = Files.readString(targetFile.toPath());
		assertEquals("this should appear in the file", result);
	}

	@Test
	void copyOverrides_whenCalledTwice() throws IOException {
		writeToTempFolder("this is the original text in the file", "overrideInputOne.tmp");
		writeToTempFolder("this is another text that is written later", "overrideInputTwo.tmp");
		final File targetFile = new File(Path.of(tempDir.toString(), "overrideResult.tmp").toUri());
		FileHelper.copyInputStreamToFile(
				new FileInputStream(Path.of(tempDir.toString(), "overrideInputOne.tmp").toFile()),
				targetFile).join();
		FileHelper.copyInputStreamToFile(
				new FileInputStream(Path.of(tempDir.toString(), "overrideInputTwo.tmp").toFile()),
				targetFile).join();
		final String result = Files.readString(targetFile.toPath());
		assertEquals("this is another text that is written later", result);
	}

	private static void writeToTempFolder(String inputText, String fileName) throws IOException {
		byte data[] = inputText.getBytes(StandardCharsets.UTF_8);
		FileOutputStream out = new FileOutputStream(Path.of(tempDir.toString(), fileName).toFile());
		out.write(data);
		out.close();
	}
}
