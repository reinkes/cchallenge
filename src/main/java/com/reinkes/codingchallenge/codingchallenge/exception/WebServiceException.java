package com.reinkes.codingchallenge.codingchallenge.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import ch.qos.logback.classic.Logger;

public abstract class WebServiceException extends Exception {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(WebServiceException.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2507494112278264120L;

	public WebServiceException() {}
	
	public WebServiceException(Exception e) {
		super(e);
		logger.info(e.getMessage());
	}

	public WebServiceException(String message) {
		super(message);
		logger.info(message);
	}

	/**
	 * HTTP status code returned in case of an exception.
	 * 
	 * @return status code
	 */
	public abstract HttpStatus getStatusCode();

}
