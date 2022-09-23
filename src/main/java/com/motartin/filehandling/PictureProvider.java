package com.motartin.filehandling;

import java.awt.Image;

/**
 * A PictureProvider provides the next Image to be used in the PicViewerApp.
 */
public interface PictureProvider {

	/**
	 * @return a {@link Image} containing the next picture to display
	 */
	public Image getNextImage();
}
