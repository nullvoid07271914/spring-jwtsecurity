package com.src.jwtsecurity.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredAccessException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public TokenExpiredAccessException(String msg) {
		super(msg);
	}

}
