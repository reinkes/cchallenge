package com.reinkes.codingchallenge.codingchallenge.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.reinkes.codingchallenge.codingchallenge.transformer.ParentTransformer;

public class ParentTransformerTest {

	@Test
	public void testSuccessEmpty() {
		Optional<String> result = new ParentTransformer().transform(Optional.empty());
		assertEquals(false, result.isPresent());
	}
	
	@Test
	public void testSuccessWithoutURLEncode() {
		Optional<String> result = new ParentTransformer().transform(Optional.of("Test"));
		assertEquals("Test", result.get());
	}
	
	@Test
	public void testSuccessWithURLEncode() {
		Optional<String> result = new ParentTransformer().transform(Optional.of("Baby%20%26%20Kleinkind"));
		assertEquals("Baby & Kleinkind", result.get());
	}
	
}
