package com.motartin.utils;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageScalerTest {

	@Test
	void whenImageFits_thenNoScalingHappens() {
		final BufferedImage source = getImage(100, 100);
		final BufferedImage target = ImageScaler.scaleToFittingImage(source.getWidth(), source.getHeight(), source);

		assertEquals(source.getWidth(), target.getWidth());
		for (int x = 0; x < source.getWidth(); x++) {
			for (int y = 0; y < source.getHeight(); y++) {
				assertEquals(source.getRGB(x, y), target.getRGB(x, y));
			}
		}
	}

	@Test
	void whenImageToWide_thenTransformedToFittingWidth() {
		final BufferedImage source = getImage(200, 100);
		final BufferedImage target = ImageScaler.scaleToFittingImage(100, 100, source);

		assertEquals(100, target.getWidth());
		assertEquals(50, target.getHeight());
	}

	@Test
	void whenImageToHigh_thenTransformedToFittingHeight() {
		final BufferedImage source = getImage(100, 200);
		final BufferedImage target = ImageScaler.scaleToFittingImage(100, 100, source);

		assertEquals(50, target.getWidth());
		assertEquals(100, target.getHeight());
	}

	@Test
	void whenImageSmallerThanScreenSize_andLandscape_thenSizeIncreased() {
		final BufferedImage source = getImage(100, 50);
		final BufferedImage target = ImageScaler.scaleToFittingImage(200, 100, source);

		assertEquals(200, target.getWidth());
		assertEquals(100, target.getHeight());
	}

	@Test
	void whenImageSmallerThanScreenSize_andPortrait_thenSizeIncreased() {
		final BufferedImage source = getImage(50, 100);
		final BufferedImage target = ImageScaler.scaleToFittingImage(100, 200, source);

		assertEquals(100, target.getWidth());
		assertEquals(200, target.getHeight());
	}

	@Test
	void whenImageHuge_thenScaledTo1080p() {
		final BufferedImage landscapeSource = getImage(3440, 1440);
		final BufferedImage landscapeTarget = ImageScaler.scaleToFittingImage(1920, 1080, landscapeSource);

		assertEquals(1920, landscapeTarget.getWidth());
		assertEquals(803, landscapeTarget.getHeight());

		final BufferedImage portraitSource = getImage(1440, 3440);
		final BufferedImage portraitTarget = ImageScaler.scaleToFittingImage(1920, 1080, portraitSource);

		assertEquals(452, portraitTarget.getWidth());
		assertEquals(1080, portraitTarget.getHeight());
	}

	private BufferedImage getImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.blue);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				g.fillRect(x, y, 1, 1);
			}
		}
		return image;
	}
}