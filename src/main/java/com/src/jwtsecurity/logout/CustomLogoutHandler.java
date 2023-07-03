package com.src.jwtsecurity.logout;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.jwtsecurity.constants.JwtTokenConstant;
import com.src.jwtsecurity.cypher.Cypher;
import com.src.jwtsecurity.jwtutils.JwtTokenUtils;

@Component
public class CustomLogoutHandler implements LogoutHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

	@Autowired
	private LogoutBlockList dao;

	@Autowired
	private JwtTokenUtils utils;

	@Autowired
	private Cypher cypher;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = request.getHeader("access_token");

		if (accessToken != null) {
			try {
				HttpSession httpSession = request.getSession();
				accessToken = accessToken.substring(JwtTokenConstant.JWT_TOKEN_BEARER.length());
				String decrypted = cypher.decrypt(httpSession, accessToken);
				accessToken = JwtTokenConstant.JWT_TOKEN_BEARER + decrypted;
				DecodedJWT decode = utils.validateJwtToken(accessToken);

				if (decode != null) {
					String username = utils.getUserNameFromJwtToken(decode);
					long expireAt = utils.getExpirationFromJwtToken(decode);
					String token = utils.extractToken(decode);

					logger.info("username: {}", username);
					logger.info("expireAt: {}", expireAt);
					logger.info("token: {}", token);

					dao.save(username, token, expireAt);

					Map<String, String> tokens = new HashMap<>();
					tokens.put("loggedout", "success");
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				}

			} catch (Exception e) {
				logger.error("throws: {}", e);
			}

			SecurityContextHolder.getContext().setAuthentication(null);
			SecurityContextHolder.clearContext();
			request.getSession().invalidate();
		}
	}
}
