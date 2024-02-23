package com.motartin.transformer;

import com.motartin.application.PictureFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Transformers {

	public static boolean fileNeedsTransformation(String fileName) {
		return fileName.toLowerCase().endsWith("orf");
	}

	public static ImageTransformer getForConfiguration() {
		final String targetFormat = System.getProperty("transformation.target.format", "jpg").toLowerCase();

		switch (targetFormat) {
			case "jpg":
			case "jpeg":
			default:
				return new ToJpegTransformer();
		}
	}

	static class ToJpegTransformer implements ImageTransformer {
		@Override
		public File convertToFittingFormat(File sourceFile, File targetFile) {

			final ProcessBuilder rawTherapeeCLI = new ProcessBuilder(
				"rawtherapee-cli",
				"-Y",//overwrite output if present
				"-o",//output file
				targetFile.getAbsolutePath(),
				"-c",//input file - has to be the last parameter
				sourceFile.getAbsolutePath());

			log.debug("Now running " + rawTherapeeCLI.command());
			try {
				final Process process = rawTherapeeCLI.start();
				int exitCode = process.waitFor();
				log.debug("Done with transformation, got exitCode " + exitCode);
			} catch (IOException | InterruptedException e) {
				log.debug("Could not transform ", e);
			}
			return targetFile;
		}
	}
}
