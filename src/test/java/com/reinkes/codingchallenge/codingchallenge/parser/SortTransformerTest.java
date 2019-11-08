package com.reinkes.codingchallenge.codingchallenge.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortingTransformer;

public class SortTransformerTest {

	@Test
	public void testSuccessEmpty() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.empty(), "default").transform();
		assertEquals(false, result.isPresent());
	}
	
	@Test
	public void testSuccessSingleEntry() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("key1"), "asc").transform();
		
		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals("key1", result.get().get(0).getKey());
		assertEquals("asc", result.get().get(0).getDirection());
	}
	
	@Test
	public void testSuccessWithoutDirection() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("key1,key2"), "asc").transform();
		
		assertTrue(result.isPresent());
		assertEquals(2, result.get().size());
		assertEquals("key1", result.get().get(0).getKey());
		assertEquals("asc", result.get().get(0).getDirection());
		assertEquals("key2", result.get().get(1).getKey());
		assertEquals("asc", result.get().get(1).getDirection());
	}
	
	@Test
	public void testSuccessWithURLEncode() {
		Optional<LinkedList<SortVO>> result = new SortingTransformer(Optional.of("key1,key2:desc,key3:asc"), "desc").transform();

		assertTrue(result.isPresent());
		assertEquals(3, result.get().size());
		assertEquals("key1", result.get().get(0).getKey());
		assertEquals("desc", result.get().get(0).getDirection());
		assertEquals("key2", result.get().get(1).getKey());
		assertEquals("desc", result.get().get(1).getDirection());
		assertEquals("key3", result.get().get(2).getKey());
		assertEquals("asc", result.get().get(2).getDirection());
	}
	
}
