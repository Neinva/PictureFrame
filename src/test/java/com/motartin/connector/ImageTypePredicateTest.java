package com.motartin.connector;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.motartin.application.Constants.PropertyKey.IMAGE_TYPES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class ImageTypePredicateTest {

	@Test
	void defaultFileEndingsWork_forPermissionCheck() {
		assertTrue(ImageTypePredicate.fileEndingPermitted("test.jpg"));
		assertTrue(ImageTypePredicate.fileEndingPermitted("test.jpeg"));
		assertFalse(ImageTypePredicate.fileEndingPermitted("test.orf"));
	}

	@Test
	void configuredFileEndingsWork_forPermissionCheck() {
		System.setProperty(IMAGE_TYPES, "jpeg,orf");
		assertFalse(ImageTypePredicate.fileEndingPermitted("test.jpg"));
		assertTrue(ImageTypePredicate.fileEndingPermitted("test.jpeg"));
		assertTrue(ImageTypePredicate.fileEndingPermitted("test.orf"));
		System.clearProperty("image.types");
	}

	@Test
	void predicateFiltersForDefaultEndings() {
		System.clearProperty(IMAGE_TYPES);
		List<String> result = Stream.of("test.jpg", "test.jpeg", "test.invalid", "test.orf")
				.filter(ImageTypePredicate.INSTANCE).toList();
		assertEquals(2, result.size());
		assertFalse(result.contains("test.orf"));
	}

	@Test
	void predicateFiltersForConfiguredEndings() {
		System.setProperty(IMAGE_TYPES, "jpg,jpeg,orf");
		List<String> result = Stream.of("test.jpg", "test.jpeg", "test.invalid", "test.orf")
				.filter(ImageTypePredicate.INSTANCE).toList();

		assertEquals(3, result.size());
		System.clearProperty("image.types");
	}

	@Test
	void whenConfiguredEndingsContainWhitespace_thenItWorks() {
		System.setProperty(IMAGE_TYPES, "jpg , jpeg , orf");
		List<String> result = Stream.of("test.jpg", "test.jpeg", "test.invalid", "test.orf")
				.filter(ImageTypePredicate.INSTANCE).toList();

		assertEquals(3, result.size());
		System.clearProperty("image.types");
	}

	@Test
	void whenEndingIsBlank_thenItWillBeIgnored() {
		System.setProperty(IMAGE_TYPES, "jpg,,jpeg");
		List<String> result = Stream.of("test.jpg", "test.jpeg", "test.invalid", "test.orf")
				.filter(ImageTypePredicate.INSTANCE).toList();

		assertEquals(2, result.size());
		System.clearProperty("image.types");
	}
}