package com.src.jwtsecurity.security.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.jwtsecurity.constants.CypherConstants;
import com.src.jwtsecurity.cypher.Cypher;
import com.src.jwtsecurity.jwtutils.JwtTokenUtils;
import com.src.jwtsecurity.model.Token;
import com.src.jwtsecurity.utils.HttpErrorResponse;

public class AuthenticationTokenSuccess extends SimpleUrlAuthenticationSuccessHandler {

	private JwtTokenUtils jwtUtils;

	private Cypher cypher;

	private HttpErrorResponse errorResponse;

	public AuthenticationTokenSuccess(JwtTokenUtils jwtUtils, Cypher cypher, HttpErrorResponse errorResponse) {
		this.jwtUtils = jwtUtils;
		this.cypher = cypher;
		this.errorResponse = errorResponse;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		String username = user.getUsername();
		String requestUri = request.getRequestURI().toString();
		String accessToken = jwtUtils.generateJwtAccessToken(user.getAuthorities(), username, requestUri);
		String refreshToken = jwtUtils.generateJwtRefreshToken(username, requestUri);

		ObjectMapper mapper = new ObjectMapper();
		OutputStream responseStream = response.getOutputStream();

		try {
			HttpSession session = request.getSession();
			accessToken = cypher.encrypt(session, accessToken);

			/* indicator for current session */
			session.setAttribute(CypherConstants.CURRENT_USERNAME, username);

			Token token = new Token();
			token.setAccessToken(accessToken);
			token.setRefreshToken(refreshToken);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.OK.value());
			mapper.writeValue(responseStream, token);
			responseStream.flush();

		} catch (Exception e) {
			logger.error("Encryption Failed: {}", e);
			errorResponse.response(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
