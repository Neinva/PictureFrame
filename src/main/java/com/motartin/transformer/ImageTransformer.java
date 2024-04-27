package com.motartin.transformer;

import java.io.File;

/**
 * When images configured and found, which cannot be displayed as-is, they can be converted to a fitting format.
 */
public interface ImageTransformer {
	public void convertToFittingFormat(File sourceFile, File targetFile);
}
