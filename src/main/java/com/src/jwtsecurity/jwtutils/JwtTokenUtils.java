package com.src.jwtsecurity.jwtutils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.src.jwtsecurity.constants.JwtTokenConstant;

@Component
public class JwtTokenUtils {

	@Value("${app.user.jwt.secret.key}")
	private String jwtSecret;

	@Value("${app.user.jwt.access.expire}")
	private int accessTokenExpires;

	@Value("${app.user.jwt.refresh.expire}")
	private int refreshTokenExpires;

	public String generateJwtAccessToken(Collection<GrantedAuthority> authorities, String username, String requestUri) {
		Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
		List<String> grantedAuthorities = authorities.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		Instant timeIssue = Instant.now();
		Instant timeExpires = timeIssue.plusSeconds(accessTokenExpires * 60);
		String token = JWT.create().withSubject(username).withIssuedAt(Date.from(timeIssue))
				.withExpiresAt(Date.from(timeExpires)).withIssuer(requestUri)
				.withClaim(JwtTokenConstant.JWT_TOKEN_ROLES, grantedAuthorities).sign(algorithm);
		return token;
	}

	public String generateJwtRefreshToken(String username, String requestUri) {
		Algorithm algorithm = Algorithm.HMAC512(jwtSecret);

		Instant timeIssue = Instant.now();
		Instant timeExpires = timeIssue.plusSeconds(refreshTokenExpires * 60);
		String token = JWT.create().withSubject(username).withIssuedAt(Date.from(timeIssue))
				.withExpiresAt(Date.from(timeExpires)).withIssuer(requestUri).sign(algorithm);
		return token;
	}

	public DecodedJWT validateJwtToken(String jwtToken) throws Exception {
		return validate(jwtToken);
	}

	public String getUserNameFromJwtToken(DecodedJWT decode) throws Exception {
		return decode.getSubject();
	}

	public long getExpirationFromJwtToken(DecodedJWT decode) throws Exception {
		Date expiryDate = decode.getExpiresAt();
		return Instant.ofEpochMilli(expiryDate.getTime()).atZone(ZoneId.systemDefault()).toEpochSecond();
	}

	public List<SimpleGrantedAuthority> getRolesFromJwtToken(DecodedJWT decode) throws Exception {
		String[] roles = decode.getClaim(JwtTokenConstant.JWT_TOKEN_ROLES).asArray(String.class);
		List<SimpleGrantedAuthority> authorities = Arrays.stream(roles).map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
		return authorities;
	}

	public String extractToken(DecodedJWT decode) {
		return decode.getToken();
	}

	private DecodedJWT validate(String jwtToken) throws Exception {
		String token = jwtToken.substring(JwtTokenConstant.JWT_TOKEN_BEARER.length());
		Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decode = verifier.verify(token);
		return decode;
	}
}
