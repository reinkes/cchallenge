package com.reinkes.codingchallenge.codingchallenge.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.reinkes.codingchallenge.codingchallenge.exception.UnexpectedParserException;
import com.reinkes.codingchallenge.codingchallenge.exception.UnknownSortingKeyException;
import com.reinkes.codingchallenge.codingchallenge.sort.SortKey;
import com.reinkes.codingchallenge.codingchallenge.sort.SortingDirection;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortingTransformer;

public class SortTransformerTest {

	private String[] validKeys = new String[] { "url", "label", "testkey" };

	@Test
	public void testSuccessEmpty() throws UnknownSortingKeyException, UnexpectedParserException {
		Optional<LinkedList<SortVO>> result = new SortingTransformer("default", validKeys)
				.transform(Optional.empty());
		assertEquals(false, result.isPresent());
	}
	
	@Test
	public void testInvalidKey() throws UnknownSortingKeyException, UnexpectedParserException {
		Optional<LinkedList<SortVO>> result = new SortingTransformer("invalidKey", validKeys)
				.transform(Optional.empty());
		assertEquals(false, result.isPresent());
	}

	@Test
	public void testSuccessSingleEntry() throws UnknownSortingKeyException, UnexpectedParserException {
		Optional<LinkedList<SortVO>> result = new SortingTransformer("asc", validKeys).transform(Optional.of("label"));

		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals(SortKey.label, result.get().get(0).getKey());
		assertEquals(SortingDirection.asc, result.get().get(0).getDirection());
	}

	@Test
	public void testSuccessWithoutDirection() throws UnknownSortingKeyException, UnexpectedParserException {
		Optional<LinkedList<SortVO>> result = new SortingTransformer("asc", validKeys)
				.transform(Optional.of("label,url"));

		assertTrue(result.isPresent());
		assertEquals(2, result.get().size());
		assertEquals(SortKey.label, result.get().get(0).getKey());
		assertEquals(SortingDirection.asc, result.get().get(0).getDirection());
		assertEquals(SortKey.url, result.get().get(1).getKey());
		assertEquals(SortingDirection.asc, result.get().get(1).getDirection());
	}

	@Test
	public void testSuccessWithURLEncode() throws UnknownSortingKeyException, UnexpectedParserException {
		Optional<LinkedList<SortVO>> result = new SortingTransformer("desc",
				validKeys).transform(Optional.of("label:asc,url:desc"));

		assertTrue(result.isPresent());
		assertEquals(2, result.get().size());
		assertEquals(SortKey.label, result.get().get(0).getKey());
		assertEquals(SortingDirection.asc, result.get().get(0).getDirection());
		assertEquals(SortKey.url, result.get().get(1).getKey());
		assertEquals(SortingDirection.desc, result.get().get(1).getDirection());
	}

}
