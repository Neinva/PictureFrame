package com.motartin.filehandling;

import com.motartin.application.PictureFrame;
import com.motartin.connector.Connector;
import com.motartin.transformer.Transformers;
import com.motartin.utils.FileHelper;
import jcifs.smb.SmbFileInputStream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
/*
  PictureProvider implementation that returns a random image
  from the list of all images accessible by the provided connector.
 */
public class RandomPicProvider implements PictureProvider {

	private final Connector connector;
	private final ExecutorService executor;
	private CompletableFuture<Image> futureIcon;

	public RandomPicProvider(@NonNull Connector connector) {
		this.connector = connector;
		executor =  Executors.newSingleThreadExecutor();
		Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdownNow));
	}

	@Override
	public Image getNextImage() {
		if (futureIcon != null && futureIcon.isDone()) {
			try {
				Image result = futureIcon.get();
				retrieveNextImage();
				return result;
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		if (futureIcon == null) {
			retrieveNextImage();
		}
		return null;
	}

	private void retrieveNextImage() {
		String nextImageName = getRandomPictureName();

		if (nextImageName == null) {
			return;
		}

		futureIcon = CompletableFuture.supplyAsync(() -> connector.getFile(nextImageName), executor)
			.thenApply(inputStream -> {
				if (inputStream instanceof SmbFileInputStream) {
					try {
						File file = Path.of(PictureFrame.getAppLocationPath(), nextImageName).toFile();
						CompletableFuture<File> fileCompletableFuture = FileHelper.copyInputStreamToFile(inputStream, file);
						fileCompletableFuture.join();
						return new FileInputStream(file);
					} catch (IOException e) {
						log.debug("Error transferring file to local file.", e);
					}
				}
				return inputStream;
			})
			.thenApply(localStream -> {
				try {
					if (Transformers.fileNeedsTransformation(nextImageName)) {
						return getTransformedImage(nextImageName);
					}
					return ImageIO.read(localStream);
				} catch (IOException e) {
					log.debug("Error during file transformation", e);
				}
				return null;
			});
	}

	private String getRandomPictureName() {
		List<String> files = connector.listFiles();
		if (files.isEmpty()) {
			return null;
		}
		int position = new SecureRandom().nextInt(files.size());
		return files.get(position);
	}

	private static BufferedImage getTransformedImage(String nextImageName) throws IOException {
		File sourceFile = new File(PictureFrame.getAppLocationPath() + "/" + nextImageName);
		File targetFile = new File(PictureFrame.getAppLocationPath() + "/transformed.jpg");
		final CompletableFuture<Void> transformation = CompletableFuture.runAsync(() -> {
			Transformers.getForConfiguration().convertToFittingFormat(sourceFile, targetFile);
		});
		transformation.join();
		return ImageIO.read(targetFile);
	}
}
