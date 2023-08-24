package com.xantrix.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex)
	{
		ErrorResponse err = new ErrorResponse();
		err.setCode(HttpStatus.NOT_FOUND.value());
		err.setMessage(ex.getMessage());
		return new  ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(BindingException.class)
	public final ResponseEntity<ErrorResponse> exceptionBindingExceptionHandler(Exception ex)
	{
		ErrorResponse err = new ErrorResponse();
		err.setCode(HttpStatus.BAD_REQUEST.value());
		err.setMessage(ex.getMessage());
		return new  ResponseEntity<ErrorResponse>(err,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(DuplicateException.class)
	public final ResponseEntity<ErrorResponse> exceptionDuplicateExceptionHandler(Exception ex)
	{
		ErrorResponse err = new ErrorResponse();
		err.setCode(HttpStatus.NOT_ACCEPTABLE.value());
		err.setMessage(ex.getMessage());
		return new  ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_ACCEPTABLE);
		
	}
	
}
