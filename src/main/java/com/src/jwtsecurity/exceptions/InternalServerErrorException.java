package com.src.jwtsecurity.exceptions;

import org.springframework.web.client.RestClientException;

public class InternalServerErrorException extends RestClientException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String msg) {
		super(msg);
	}

}
