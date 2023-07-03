package com.src.jwtsecurity.exceptions;

import org.springframework.web.client.RestClientException;;

public class BadRequestException extends RestClientException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String msg) {
		super(msg);
	}
}
