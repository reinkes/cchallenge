package com.reinkes.codingchallenge.codingchallenge.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinkes.codingchallenge.codingchallenge.CodingchallengeApplication;
import com.reinkes.codingchallenge.codingchallenge.domain.output.ResultVO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CodingchallengeApplication.class)
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class ControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testEmptyApiResponse() throws Exception {
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>("", HttpStatus.OK));
		MvcResult result = sendRequest(null);
		
		ArrayList<ResultVO> resultBody = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		assertTrue(resultBody.isEmpty());
	}
	
	@Test
	public void testMinimumSuccess() throws Exception {
		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest(null);
		
		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		
		assertEquals(5, resultBody.length);
		
		assertEquals("Sortiment - Alter - Baby & Kleinkind", resultBody[0].getLabel());
		assertEquals("http://www.mytoys.de/0-6-months/", resultBody[0].getUrl());
		
		assertEquals("Sortiment - Alter - Baby & Kleinkind", resultBody[1].getLabel());
		assertEquals("http://www.mytoys.de/7-12-months/", resultBody[1].getUrl());
		
		assertEquals("Sortiment - Alter - Baby & Kleinkind", resultBody[2].getLabel());
		assertEquals("http://www.mytoys.de/13-24-months/", resultBody[2].getUrl());
		
		assertEquals("Sortiment - Alter - Kindergarten", resultBody[3].getLabel());
		assertEquals("http://www.mytoys.de/24-47-months/", resultBody[3].getUrl());
		
		assertEquals("Sortiment - Alter - Kindergarten", resultBody[4].getLabel());
		assertEquals("http://www.mytoys.de/48-71-months/", resultBody[4].getUrl());
	}
	
	@Test
	public void testFilterSuccess() throws Exception {
		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=Kindergarten");
		
		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		
		assertEquals(2, resultBody.length);
		
		assertEquals("Kindergarten", resultBody[0].getLabel());
		assertEquals("http://www.mytoys.de/24-47-months/", resultBody[0].getUrl());
		
		assertEquals("Kindergarten", resultBody[1].getLabel());
		assertEquals("http://www.mytoys.de/48-71-months/", resultBody[1].getUrl());
	}
	
	@Test
	public void testFilterSuccessWithURLEncode() throws Exception {
		fail();
	}

	@Test
	public void testSingleSortSuccess() throws Exception {
		fail();
	}

	@Test
	public void testMultiSortSuccess() throws Exception {
		fail();
	}
	
	private MvcResult sendRequest(String suffix) throws Exception {
		String url = "http://localhost:" + port + "/links" + (!StringUtils.isEmpty(suffix) ? suffix : "");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, url)
				.contentType(MediaType.APPLICATION_JSON);
		return mockMvc.perform(requestBuilder).andReturn();
	}
	
	private String readFile(String fileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)));
	}
}
