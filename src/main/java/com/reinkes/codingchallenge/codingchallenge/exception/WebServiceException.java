package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public abstract class WebServiceException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2507494112278264120L;

	public WebServiceException() {}
	
	public WebServiceException(Exception e) {
		super(e);
	}

	public WebServiceException(String message) {
		super(message);
	}

	/**
	 * HTTP status code returned in case of an exception.
	 * 
	 * @return status code
	 */
	public abstract HttpStatus getStatusCode();

}
