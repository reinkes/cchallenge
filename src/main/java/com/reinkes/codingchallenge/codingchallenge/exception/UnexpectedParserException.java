package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedParserException extends WebServiceException {

	private static final long serialVersionUID = -2125250984132619676L;

	public UnexpectedParserException(Exception e) {
		super(e);
	}

	public UnexpectedParserException(String msg) {
		super(msg);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
