package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;

public class NoResultException extends WebServiceException {

	private static final long serialVersionUID = -6333214585186411828L;

	public NoResultException(String message) {
		super(message);
	}

	public NoResultException() {
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}
}
