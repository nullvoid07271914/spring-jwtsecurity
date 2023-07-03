package com.src.jwtsecurity.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.jwtsecurity.model.UserCredential;
import com.src.jwtsecurity.utils.HttpErrorResponse;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	private AuthenticationManager authenticationManager;

	private HttpErrorResponse errorResponse;

	public AuthenticationTokenFilter(AuthenticationManager manager, HttpErrorResponse errorResponse) {
		this.authenticationManager = manager;
		this.errorResponse = errorResponse;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserCredential credential = new ObjectMapper().readValue(request.getInputStream(), UserCredential.class);
			String username = credential.getUsername();
			String password = credential.getPassword();
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			return this.authenticationManager.authenticate(authToken);
		} catch (AuthenticationException e) {
			logger.error("AuthenticationException: {}", e);
			throw e;
		} catch (JacksonException e) {
			logger.error("JacksonException: {}", e);
			errorResponse.response(response, HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Exception: {}", e);
			errorResponse.response(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		return null;
	}
}
