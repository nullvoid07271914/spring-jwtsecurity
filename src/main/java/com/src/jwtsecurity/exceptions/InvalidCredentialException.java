package com.src.jwtsecurity.exceptions;

import org.springframework.web.client.RestClientException;

public class InvalidCredentialException extends RestClientException {

	private static final long serialVersionUID = 1L;

	public InvalidCredentialException(String msg) {
		super(msg);
	}
}
