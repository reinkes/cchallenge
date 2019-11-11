package com.reinkes.codingchallenge.codingchallenge.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import com.reinkes.codingchallenge.codingchallenge.AbstractTest;
import com.reinkes.codingchallenge.codingchallenge.CodingchallengeApplication;
import com.reinkes.codingchallenge.codingchallenge.domain.output.ResultVO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = CodingchallengeApplication.class)
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class ControllerTest extends AbstractTest {

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

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());

		String resultBody = result.getResponse().getContentAsString();
		assertTrue(resultBody.contains("No content to map due to end-of-input"));
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

		String[] expLabels = new String[] { "Sortiment - Alter - Baby & Kleinkind",
				"Sortiment - Alter - Baby & Kleinkind", "Sortiment - Alter - Baby & Kleinkind",
				"Sortiment - Alter - Kindergarten", "Sortiment - Alter - Kindergarten" };

		String[] expLinks = new String[] { "http://www.mytoys.de/0-6-months/", "http://www.mytoys.de/7-12-months/",
				"http://www.mytoys.de/13-24-months/", "http://www.mytoys.de/24-47-months/",
				"http://www.mytoys.de/48-71-months/" };
		checkExpectedResultVOs(resultBody, expLabels, expLinks);
	}

	@Test
	public void testFilterSuccess() throws Exception {
		String filter = "Kindergarten";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=" + filter);

		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(2, resultBody.length);

		String[] expLinks = new String[] { "http://www.mytoys.de/24-47-months/", "http://www.mytoys.de/48-71-months/" };
		checkExpectedResultVOs(resultBody, filter, expLinks);
	}

	@Test
	public void testFilterExceptionWithoutURLEncode() throws Exception {
		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=Baby & Kleinkind");

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		String resultBody = result.getResponse().getContentAsString();

		assertEquals("No results found for parent: Baby ", resultBody);
	}

	@Test
	public void testFilterSuccessWithURLEncode() throws Exception {
		String categoryToFilter = "Baby & Kleinkind";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8"));

		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(3, resultBody.length);

		String[] expLinks = new String[] { "http://www.mytoys.de/0-6-months/", "http://www.mytoys.de/7-12-months/",
				"http://www.mytoys.de/13-24-months/" };
		checkExpectedResultVOs(resultBody, categoryToFilter, expLinks);
	}

	private void checkExpectedResultVOs(ResultVO[] result, String expLabel, String[] expLinks) {
		checkExpectedResultVOs(result, new String[] { expLabel }, expLinks);
	}

	private void checkExpectedResultVOs(ResultVO[] result, String[] expLabels, String[] expLinks) {
		for (int i = 0; i < result.length; i++) {
			String expLabel = expLabels.length == 1 ? expLabels[0] : expLabels[i];
			String expLink = expLinks[i];

			assertEquals(expLabel, result[i].getLabel());
			assertEquals(expLink, result[i].getUrl());
		}
	}

	@Test
	public void testSingleSortSuccess() throws Exception {
		// sorting with asc
		String categoryToFilter = "Baby & Kleinkind";
		String sort = "url:asc";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8") + "&sort=" + sort);

		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(3, resultBody.length);

		String[] expLinks = new String[] { "http://www.mytoys.de/0-6-months/", "http://www.mytoys.de/13-24-months/",
				"http://www.mytoys.de/7-12-months/", };
		checkExpectedResultVOs(resultBody, categoryToFilter, expLinks);

		// sorting with desc
		sort = "url:desc";
		result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8") + "&sort=" + sort);

		resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(3, resultBody.length);

		expLinks = new String[] { "http://www.mytoys.de/7-12-months/", "http://www.mytoys.de/13-24-months/",
				"http://www.mytoys.de/0-6-months/", };
		checkExpectedResultVOs(resultBody, categoryToFilter, expLinks);
	}

	@Test
	public void testUnknownSortException() throws Exception {
		// sorting with asc
		String categoryToFilter = "Baby & Kleinkind";
		String sort = "test:asc";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8") + "&sort=" + sort);

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		assertEquals("Unknown sorting key: test", result.getResponse().getContentAsString());
	}
	
	@Test
	public void testNoResultException() throws Exception {
		String categoryToFilter = "test";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		MvcResult result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8"));

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		assertEquals("No results found for parent: test", result.getResponse().getContentAsString());
	}

	@Test
	public void testMultiSortSuccess() throws Exception {
		// First sorting: label desc and url asc
		String categoryToFilter = "Sortiment";
		String sort = "label:desc,url:asc";

		String mockResponse = readFile("simpleResponse.txt");
		Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>(mockResponse, HttpStatus.OK));
		
		MvcResult result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8") + "&sort=" + sort);
		ResultVO[] resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(5, resultBody.length);

		String[] expLabels = new String[] { 
				"Sortiment - Alter - Kindergarten", 
				"Sortiment - Alter - Kindergarten", 
				"Sortiment - Alter - Baby & Kleinkind",
				"Sortiment - Alter - Baby & Kleinkind", 
				"Sortiment - Alter - Baby & Kleinkind",
				};

		String[] expLinks = new String[] { 
				"http://www.mytoys.de/24-47-months/",
				"http://www.mytoys.de/48-71-months/", 
				"http://www.mytoys.de/0-6-months/", 
				"http://www.mytoys.de/13-24-months/", 
				"http://www.mytoys.de/7-12-months/",
				};
		checkExpectedResultVOs(resultBody, expLabels, expLinks);
		
		// First sorting: label asc and url desc
		sort = "label:asc,url:desc";
		result = sendRequest("?parent=" + URLEncoder.encode(categoryToFilter, "UTF-8") + "&sort=" + sort);
		resultBody = mapper.readValue(result.getResponse().getContentAsString(), ResultVO[].class);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		assertEquals(5, resultBody.length);

		expLabels = new String[] { 
				"Sortiment - Alter - Baby & Kleinkind",
				"Sortiment - Alter - Baby & Kleinkind", 
				"Sortiment - Alter - Baby & Kleinkind",
				"Sortiment - Alter - Kindergarten", 
				"Sortiment - Alter - Kindergarten", 
				};

		expLinks = new String[] { 
				"http://www.mytoys.de/7-12-months/",
				"http://www.mytoys.de/13-24-months/", 
				"http://www.mytoys.de/0-6-months/", 
				"http://www.mytoys.de/48-71-months/", 
				"http://www.mytoys.de/24-47-months/",
				};
		checkExpectedResultVOs(resultBody, expLabels, expLinks);
	}

	private MvcResult sendRequest(String suffix) throws Exception {
		String url = "http://localhost:" + port + "/links" + (!StringUtils.isEmpty(suffix) ? suffix : "");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, url)
				.contentType(MediaType.APPLICATION_JSON);
		return mockMvc.perform(requestBuilder).andReturn();
	}
}
