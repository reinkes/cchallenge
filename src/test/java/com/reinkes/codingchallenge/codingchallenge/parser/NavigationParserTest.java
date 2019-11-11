package com.reinkes.codingchallenge.codingchallenge.parser;


import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinkes.codingchallenge.codingchallenge.AbstractTest;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Navigation;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Node;
import com.reinkes.codingchallenge.codingchallenge.domain.input.NodeType;

public class NavigationParserTest extends AbstractTest {
	
	@Test
	public void testNavigationParsing() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Navigation navigation = mapper.readValue(readFile("simpleResponse.txt"), Navigation.class);

		// check navigation node
		assertEquals(1, navigation.getNavigationEntries().size());
		Node node = navigation.getNavigationEntries().get(0);
		
		// check section node
		assertEquals(NodeType.section, node.getType());
		assertEquals("Sortiment", node.getLabel());
		assertEquals(1, node.getChildren().size());
		assertNull(node.getUrl());
	
		// check for existing node-type => see testNodeParsing()
		Node node2 = node.getChildren().get(0);
		assertEquals(NodeType.node, node2.getType());
	}
	
	@Test
	public void testLinkParsing() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Node link = mapper.readValue(readFile("exampleLink.txt"), Node.class);
		
		assertEquals(NodeType.link, link.getType());
		assertEquals("4-5 Jahre", link.getLabel());
		assertEquals("http://www.mytoys.de/48-71-months/", link.getUrl());
	}
	
	@Test
	public void testNodeParsing() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Node node = mapper.readValue(readFile("exampleNode.txt"), Node.class);
		
		assertEquals(NodeType.node, node.getType());
		assertEquals("Baby & Kleinkind", node.getLabel());
		assertEquals(3, node.getChildren().size());
		assertNull(node.getUrl());
	}
	
}
