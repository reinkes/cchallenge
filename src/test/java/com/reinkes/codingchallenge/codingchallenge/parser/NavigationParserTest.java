package com.reinkes.codingchallenge.codingchallenge.parser;


import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinkes.codingchallenge.codingchallenge.domain.Navigation;
import com.reinkes.codingchallenge.codingchallenge.domain.Node;

public class NavigationParserTest {

	// TODO: put this in a file...
	public static String exampleJson = "{\r\n" + 
			"  \"navigationEntries\": [{\r\n" + 
			"    \"type\": \"section\",\r\n" + 
			"    \"label\": \"Sortiment\",\r\n" + 
			"    \"children\": [{\r\n" + 
			"      \"type\": \"node\",\r\n" + 
			"      \"label\": \"Alter\",\r\n" + 
			"      \"children\": [{\r\n" + 
			"        \"type\": \"node\",\r\n" + 
			"        \"label\": \"Baby & Kleinkind\",\r\n" + 
			"        \"children\": [{\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"0-6 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/0-6-months\\/\"\r\n" + 
			"        }, {\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"7-12 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/7-12-months\\/\"\r\n" + 
			"        }, {\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"13-24 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/13-24-months\\/\"\r\n" + 
			"        }]\r\n" + 
			"      }, {\r\n" + 
			"        \"type\": \"node\",\r\n" + 
			"        \"label\": \"Kindergarten\",\r\n" + 
			"        \"children\": [{\r\n" + 
			"          \"type\": \"link\",\r\n" + 
			"          \"label\": \"2-3 Jahre\",\r\n" + 
			"          \"url\": \"http:\\/\\/www.mytoys.de\\/24-47-months\\/\"\r\n" + 
			"        }, {\r\n" + 
			"          \"type\": \"link\",\r\n" + 
			"          \"label\": \"4-5 Jahre\",\r\n" + 
			"          \"url\": \"http:\\/\\/www.mytoys.de\\/48-71-months\\/\"\r\n" + 
			"        }]\r\n" + 
			"      }]\r\n" + 
			"    }]\r\n" + 
			"  }]\r\n" + 
			"}";
	
	public static String exampleLink = "{\r\n" + 
			"          \"type\": \"link\",\r\n" + 
			"          \"label\": \"4-5 Jahre\",\r\n" + 
			"          \"url\": \"http:\\/\\/www.mytoys.de\\/48-71-months\\/\"\r\n" + 
			"        }";
	
	public static String exampleNode = "{\r\n" + 
			"        \"type\": \"node\",\r\n" + 
			"        \"label\": \"Baby & Kleinkind\",\r\n" + 
			"        \"children\": [{\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"0-6 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/0-6-months\\/\"\r\n" + 
			"        }, {\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"7-12 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/7-12-months\\/\"\r\n" + 
			"        }, {\r\n" + 
			"            \"type\": \"link\",\r\n" + 
			"            \"label\": \"13-24 Monate\",\r\n" + 
			"            \"url\": \"http:\\/\\/www.mytoys.de\\/13-24-months\\/\"\r\n" + 
			"        }]\r\n" + 
			"      }";
	
	@Test
	public void testNavigationParsing() throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Navigation navigation = mapper.readValue(exampleJson, Navigation.class);

		// check navigation node
		assertEquals(1, navigation.getNavigationEntries().size());
		Node node = navigation.getNavigationEntries().get(0);
		
		// check section node
		assertEquals("section", node.getType());
		assertEquals("Sortiment", node.getLabel());
		assertEquals(1, node.getChildren().size());
		assertNull(node.getUrl());
	
		// check for existing node-type => see testNodeParsing()
		Node node2 = node.getChildren().get(0);
		assertEquals("node", node2.getType());
	}
	
	@Test
	public void testLinkParsing() throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Node link = mapper.readValue(exampleLink, Node.class);
		
		assertEquals("link", link.getType());
		assertEquals("4-5 Jahre", link.getLabel());
		assertEquals("http://www.mytoys.de/48-71-months/", link.getUrl());
	}
	
	@Test
	public void testNodeParsing() throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Node node = mapper.readValue(exampleNode, Node.class);
		
		assertEquals("node", node.getType());
		assertEquals("Baby & Kleinkind", node.getLabel());
		assertEquals(3, node.getChildren().size());
		assertNull(node.getUrl());
	}
	
}
