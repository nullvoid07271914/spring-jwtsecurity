package com.src.jwtsecurity.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.info("authentication: {}", authentication);
		if (authentication != null && authentication.getDetails() != null) {
			try {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				String username = userDetails.getUsername();
				logger.info("{} Successfully logged out.", username);

			} catch (IllegalStateException e) {
				logger.error("IllegalStateException: {}", e);
			}
		}
	}
}
