package com.src.jwtsecurity.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.src.jwtsecurity.constants.CypherConstants;
import com.src.jwtsecurity.constants.JwtTokenConstant;
import com.src.jwtsecurity.constants.RequestUri;
import com.src.jwtsecurity.cypher.Cypher;
import com.src.jwtsecurity.domain.User;
import com.src.jwtsecurity.jwtutils.JwtTokenUtils;
import com.src.jwtsecurity.logout.LogoutBlockList;
import com.src.jwtsecurity.repositories.UserRepo;
import com.src.jwtsecurity.utils.HttpErrorResponse;

@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationTokenFilter.class);

	@Autowired
	private JwtTokenUtils jwtUtils;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private LogoutBlockList logoutBlockList;

	@Autowired
	private HttpErrorResponse errorResponse;

	@Autowired
	private Cypher cypher;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerToken = parseHeaderToken(request);
		if (headerToken != null) {

			HttpSession session = request.getSession();
			String currentUser = (String) session.getAttribute(CypherConstants.CURRENT_USERNAME);
			logger.info("currentUser: {}", currentUser);

			if (currentUser == null) {
				errorResponse.response(response, HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				try {
					String requestUri = request.getRequestURI().toString();
					String accessToken = parseAccessToken(session, headerToken);
					DecodedJWT decode = isValidAccessToken(accessToken);
					if (decode != null) {
						boolean loggedIn = isCurrentLoggedIn(decode, currentUser);
						if (loggedIn) {
							if (!isAlreadyLogout(currentUser, accessToken)) {
								List<SimpleGrantedAuthority> authorities = null;

								if (requestUri.equals(RequestUri.REFRESH_TOKEN)) {
									User user = userRepo.findRolesByUsername(currentUser);
									authorities = user.getRoles().stream()
											.map(role -> new SimpleGrantedAuthority(role.getName()))
											.collect(Collectors.toList());
								} else {
									authorities = jwtUtils.getRolesFromJwtToken(decode);
								}

								UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
										currentUser, null, authorities);
								SecurityContextHolder.getContext().setAuthentication(authToken);
							}
						}
					}

				} catch (JWTVerificationException e) {
					/* expired token */
					logger.error("JWTVerificationException: {}", e);
					throw e;
				} catch (BadPaddingException e) {
					/* never been login */
					logger.error("BadPaddingException: {}", e);
					errorResponse.response(response, HttpServletResponse.SC_UNAUTHORIZED);
				} catch (Exception e) {
					logger.error("Exception: {}", e);
					errorResponse.response(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isAlreadyLogout(String username, String accessToken) {
		return logoutBlockList.verifyExist(username, accessToken);
	}

	private DecodedJWT isValidAccessToken(String accessToken) throws Exception {
		return jwtUtils.validateJwtToken(accessToken);
	}

	private boolean isCurrentLoggedIn(DecodedJWT decode, String currentUser) throws Exception {
		String username = jwtUtils.getUserNameFromJwtToken(decode);
		return username.equals(currentUser);
	}

	private String parseHeaderToken(HttpServletRequest request) {
		String headerAuthToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (headerAuthToken != null && headerAuthToken.startsWith(JwtTokenConstant.JWT_TOKEN_BEARER)) {
			return headerAuthToken.substring(JwtTokenConstant.JWT_TOKEN_BEARER.length());
		}
		return null;
	}

	private String parseAccessToken(HttpSession session, String headerAuthToken) throws Exception {
		String decrypted = cypher.decrypt(session, headerAuthToken);
		return JwtTokenConstant.JWT_TOKEN_BEARER + decrypted;
	}
}
