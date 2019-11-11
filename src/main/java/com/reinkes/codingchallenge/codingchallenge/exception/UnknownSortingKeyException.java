package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public class UnknownSortingKeyException extends WebServiceException {

	private static final long serialVersionUID = -6333214585186411828L;
	private String key;

	public UnknownSortingKeyException(String key) {
		this.key = key;
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getMessage() {
		return String.format("Unknown sorting key: %s", this.key);
	}

}
