package com.src.jwtsecurity.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccessDeniedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException(String message) {
		super(message);
	}
}
