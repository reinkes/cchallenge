package com.reinkes.codingchallenge.codingchallenge.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortingTransformer;

public class SortTransformerTest {

	private String[] validKeys = new String[] { "url", "label", "testkey" };

	@Test
	public void testSuccessEmpty() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.empty(), "default", validKeys)
				.transform();
		assertEquals(false, result.isPresent());
	}
	
	@Test
	public void testInvalidKey() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.empty(), "invalidKey", validKeys)
				.transform();
		assertEquals(false, result.isPresent());
	}

	@Test
	public void testSuccessSingleEntry() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("label"), "asc", validKeys).transform();

		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals("label", result.get().get(0).getKey());
		assertEquals("asc", result.get().get(0).getDirection());
	}

	@Test
	public void testSuccessWithoutDirection() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("label,url"), "asc", validKeys)
				.transform();

		assertTrue(result.isPresent());
		assertEquals(2, result.get().size());
		assertEquals("label", result.get().get(0).getKey());
		assertEquals("asc", result.get().get(0).getDirection());
		assertEquals("url", result.get().get(1).getKey());
		assertEquals("asc", result.get().get(1).getDirection());
	}

	@Test
	public void testSuccessWithURLEncode() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("label,url:desc,testkey:asc"), "desc",
				validKeys).transform();

		assertTrue(result.isPresent());
		assertEquals(3, result.get().size());
		assertEquals("label", result.get().get(0).getKey());
		assertEquals("desc", result.get().get(0).getDirection());
		assertEquals("url", result.get().get(1).getKey());
		assertEquals("desc", result.get().get(1).getDirection());
		assertEquals("testkey", result.get().get(2).getKey());
		assertEquals("asc", result.get().get(2).getDirection());
	}

}
