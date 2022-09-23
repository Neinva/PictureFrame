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
			final ProcessBuilder transformerProgram = new ProcessBuilder("magick", " " + sourceFile.getAbsolutePath() + " " + targetFile.getAbsolutePath());
			log.debug("Now running " + transformerProgram.command());
			transformerProgram.directory(new File(PictureFrame.getAppLocationPath()));
			try {
				final Process process = transformerProgram.start();
				int exitCode = process.waitFor();
				log.debug("done with transformation, got exitCode " + exitCode);
			} catch (IOException | InterruptedException e) {
				log.debug("Could not transform ", e);
			}
			return targetFile;
		}
	}
}
