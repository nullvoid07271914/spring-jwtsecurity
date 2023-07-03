package com.src.jwtsecurity.auth.failure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.src.jwtsecurity.utils.HttpErrorResponse;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationFailureHandler.class);

	private HttpErrorResponse errorResponse;

	public LoginAuthenticationFailureHandler(HttpErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		logger.error("AuthenticationException: {}", exception);
		errorResponse.response(response, HttpServletResponse.SC_BAD_REQUEST);
	}
}
