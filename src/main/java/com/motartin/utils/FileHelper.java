package com.motartin.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
public class FileHelper {
	public static CompletableFuture<File> copyInputStreamToFile(@NonNull InputStream inputStream, @NonNull File file) {
		return CompletableFuture.supplyAsync(() -> {
			try (OutputStream output = new FileOutputStream(file)) {
				log.debug("Copy input stream to " + file.getAbsolutePath());
				inputStream.transferTo(output);
			} catch (IOException ioException) {
				log.debug(ioException.getMessage());
			}
			return file;
		}, Executors.newSingleThreadExecutor());
	}

	private FileHelper() {
		throw new AssertionError(getClass().getSimpleName() + " cannot be instantiated.");
	}
}
