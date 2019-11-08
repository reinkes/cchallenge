package com.reinkes.codingchallenge.codingchallenge.transformer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import ch.qos.logback.classic.Logger;

public class ParentTransformer {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ParentTransformer.class);
	
	public Optional<String> transform(String parent) {
		return urlDecode(parent);
	}

	private Optional<String> urlDecode(String parent) {
		try {
			if(StringUtils.isEmpty(parent)) {
				return Optional.empty();
			}
			
			parent = URLDecoder.decode(parent, "UTF-8");
			logger.debug("URL decode of parent: " + parent);
			return Optional.of(parent);
		} catch (UnsupportedEncodingException e) {
			logger.debug("Exception in URLDecode thrown: " + e.getMessage());
		}
		return Optional.empty();
	}
	
}
