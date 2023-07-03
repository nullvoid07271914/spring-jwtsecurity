package com.src.jwtsecurity.exceptions;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.src.jwtsecurity.model.RestError;
import com.src.jwtsecurity.utils.HttpErrorResponse;

@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private HttpErrorResponse errorResponse;

	@ExceptionHandler({ AuthenticationException.class, AccessDeniedAuthenticationException.class })
	@ResponseBody
	public ResponseEntity<RestError> authenticationException(Exception ex) {
		RestError error = errorResponse.createError(HttpServletResponse.SC_UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
}
