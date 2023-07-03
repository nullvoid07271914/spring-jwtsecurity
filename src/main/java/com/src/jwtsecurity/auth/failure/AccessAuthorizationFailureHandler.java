package com.src.jwtsecurity.auth.failure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.src.jwtsecurity.utils.HttpErrorResponse;

public class AccessAuthorizationFailureHandler implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(AccessAuthorizationFailureHandler.class);

	private HttpErrorResponse errorResponse;

	public AccessAuthorizationFailureHandler(HttpErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		logger.error("AccessDeniedException: {}", exception);
		errorResponse.response(response, HttpServletResponse.SC_UNAUTHORIZED);
	}
}
