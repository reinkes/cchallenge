package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public class UnknownSortingKeyException extends WebServiceException {

	private static final long serialVersionUID = -6333214585186411828L;

	public UnknownSortingKeyException(String msg) {
		super(msg);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}
}
