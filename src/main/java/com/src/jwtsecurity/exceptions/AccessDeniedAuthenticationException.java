package com.src.jwtsecurity.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccessDeniedAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedAuthenticationException(String message) {
		super(message);
	}
}
