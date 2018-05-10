package com.user.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionMapper {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> exceptionUserHandler(UserException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setSuccess(false);
		error.setErrorCode(ex.getCode());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		error.setMessage("The request could not be understood by the server due to malformed syntax.");
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
}
