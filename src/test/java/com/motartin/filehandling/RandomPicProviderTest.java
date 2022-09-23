package com.motartin.filehandling;

import com.motartin.connector.DefaultConnector;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class RandomPicProviderTest {

	static DefaultConnector connector = mock(DefaultConnector.class);

	@BeforeAll
	static void prepareTests() {
		when(connector.listFiles()).thenReturn(List.of("first.jpg", "second.jpg", "third.jpg"));
		when(connector.getFile("first.jpg")).thenReturn(new ByteArrayInputStream("first.jpg".getBytes()));
		when(connector.getFile("second.jpg")).thenReturn(new ByteArrayInputStream("second.jpg".getBytes()));
		when(connector.getFile("third.jpg")).thenReturn(new ByteArrayInputStream("third.jpg".getBytes()));
	}

	@Test
	void creatingRandomPicProvider_withoutConnector_throwsException() {
		assertThrows(NullPointerException.class, () -> new RandomPicProvider(null));
	}
}