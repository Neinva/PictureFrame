package com.motartin.transformer;

import java.io.File;

/**
 * Deal with improper formats
 */
public interface ImageTransformer {
	public File convertToFittingFormat(File sourceFile, File targetFile);
}
