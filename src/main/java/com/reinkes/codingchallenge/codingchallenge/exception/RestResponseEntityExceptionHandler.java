package com.reinkes.codingchallenge.codingchallenge.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = WebServiceException.class)
	public static ResponseEntity<Object> handleWSException(WebServiceException e) {
		return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
	}
}
