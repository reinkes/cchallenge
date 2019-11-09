package com.reinkes.codingchallenge.codingchallenge.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinkes.codingchallenge.codingchallenge.CodingchallengeApplication;
import com.reinkes.codingchallenge.codingchallenge.domain.api.ApiService;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Navigation;
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
	private ApiService apiService;

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testEmptyApiResponse() throws Exception {
		Mockito.when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(Class.class)))
				.thenReturn(new ResponseEntity<String>("", HttpStatus.OK));

		String url = "http://localhost:" + port + "/links";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, url)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		ArrayList<ResultVO> resultBody = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		assertTrue(resultBody.isEmpty());
	}
}
