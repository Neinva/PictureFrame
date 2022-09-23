package com.motartin.connector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.motartin.application.Constants.App.DEFAULT_IMAGE_TYPES;
import static com.motartin.application.Constants.PropertyKey.IMAGE_TYPES;

/**
 * Predicate used to filter all files based on file ending.
 * Takes all accepts all file endings as configured in the property image.types or jpg and jpeg as default.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageTypePredicate implements Predicate<String> {

	public static Predicate<String> INSTANCE = new ImageTypePredicate();

	@Override
	public boolean test(String fileName) {
		return fileEndingPermitted(fileName);
	}

	static boolean fileEndingPermitted(String fileName) {
		final String imageTypes = System.getProperty(IMAGE_TYPES, DEFAULT_IMAGE_TYPES);
		final String typesToVerify = imageTypes.isBlank() ? DEFAULT_IMAGE_TYPES : imageTypes;

		final List<String> fileEndings = Arrays.stream(typesToVerify.split(","))
				.filter(ending -> !ending.isBlank())
				.map(String::trim)
				.toList();
		log.debug("filtering for {}", fileEndings);
		return fileEndings.stream().anyMatch(fileName.toLowerCase()::endsWith);
	}
}
