package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedParserException extends WebServiceException {

	private static final long serialVersionUID = -2125250984132619676L;

	public UnexpectedParserException(Exception e) {
		super(e);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getMessage() {
		return String.format("Unexpected parsing error");
	}
}
