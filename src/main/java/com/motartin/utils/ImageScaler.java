package com.motartin.utils;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@Slf4j
public class ImageScaler {

	public static BufferedImage scaleToFittingImage(int availableWidth, int availableHeight, BufferedImage nextPicture) {
		double imageAspectRatio = (double) nextPicture.getWidth() / nextPicture.getHeight();
		double screenAspectRatio = (double) availableWidth / availableHeight;

		int targetWidth;
		int targetHeight;

		if (screenAspectRatio > imageAspectRatio) {
			targetWidth = (int) Math.floor(nextPicture.getWidth() * (availableHeight * 1.0 / nextPicture.getHeight()));
			targetHeight = availableHeight;
		} else {
			targetWidth = availableWidth;
			targetHeight = (int) Math.floor(nextPicture.getHeight() * (availableWidth * 1.0 / nextPicture.getWidth()));
		}

		return Scalr.resize(nextPicture, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight);
	}

	private ImageScaler() {
		throw new AssertionError(getClass().getSimpleName() + " cannot be instantiated.");
	}
}
