package com.reinkes.codingchallenge.codingchallenge.domain.api;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Navigation;

import ch.qos.logback.classic.Logger;

@Component
public class ApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${api.key}")
	private String apiKey;

	@Value("${api.url}")
	private String apiURL;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ApiService.class);
	
	public Optional<Navigation> fetchDataFromAPI() {
		try {
			ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, buildHttpEntity(),
					String.class);
			if (response != null && response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper mapper = new ObjectMapper();
				Navigation navigation = mapper.readValue(response.getBody(), Navigation.class);
				return Optional.of(navigation);
			}
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		logger.error("Either the API returned an error... ");
		return Optional.empty();
	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	private HttpEntity<?> buildHttpEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", apiKey);
		return new HttpEntity(headers);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
