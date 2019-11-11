package com.reinkes.codingchallenge.codingchallenge.transformer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ParentTransformer {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ParentTransformer.class);

	public ParentTransformer() {
	}

	public Optional<String> transform(Optional<String> parent) {
		return urlDecode(parent);
	}

	private Optional<String> urlDecode(Optional<String> parent) {
		try {
			if (!parent.isPresent()) {
				return parent;
			}

			String decodedParent = URLDecoder.decode(parent.get(), "UTF-8");
			logger.debug("URL decode of parent: " + decodedParent);
			return Optional.of(decodedParent);
		} catch (UnsupportedEncodingException e) {
			logger.debug("Exception in URLDecode thrown: " + e.getMessage());
		}
		return Optional.empty();
	}

}
