package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class NoResultException extends WebServiceException {

	private static final long serialVersionUID = -6333214585186411828L;
	private String parent;

	public NoResultException(String message, String parent) {
		super(message);
		this.parent = parent;
	}

	public NoResultException() {
	}

	public NoResultException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getMessage() {
		if(!StringUtils.isEmpty(parent)) {
			return String.format("No results found for parent: %s", this.parent);
		} else {
			return String.format("No results found");
		}
	}
}
