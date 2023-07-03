package com.src.jwtsecurity.api;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.jwtsecurity.domain.Role;
import com.src.jwtsecurity.domain.User;
import com.src.jwtsecurity.jwtutils.JwtTokenUtils;
import com.src.jwtsecurity.service.UserService;

@RestController
@RequestMapping("/api")
public class UserApiResource {

	private static final Logger logger = LoggerFactory.getLogger(UserApiResource.class);

	@Autowired
	private UserService service;

	@Autowired
	private JwtTokenUtils jwtUtils;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = service.getUsers();
		logger.info("users = {}", users);

		return ResponseEntity.ok().body(service.getUsers());
	}

	@PostMapping("/user/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		String uriPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString();
		logger.info("uriPath = {}", uriPath);

		URI uri = URI.create(uriPath);
		return ResponseEntity.created(uri).body(service.saveUser(user));
	}

	@PostMapping("/role/save")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		String uriPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString();
		logger.info("uriPath = {}", uriPath);

		URI uri = URI.create(uriPath);
		return ResponseEntity.created(uri).body(service.saveRole(role));
	}

	@PostMapping("/role/addToUser")
	public ResponseEntity<Role> addRoleToUser(@RequestBody RoleUserForm form) {
		service.addRoleToUser(form.getUsername(), form.getRolename());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String authHeaderToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeaderToken != null) {
			DecodedJWT decode = jwtUtils.validateJwtToken(authHeaderToken);
			if (decode != null) {
				String currentUsername = jwtUtils.getUserNameFromJwtToken(decode);
				String requestUri = request.getRequestURI().toString();
				User user = service.getUser(currentUsername);
				List<GrantedAuthority> authorities = user.getRoles().stream()
						.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

				String accessToken = jwtUtils.generateJwtAccessToken(authorities, currentUsername, requestUri);
				String refreshToken = jwtUtils.generateJwtRefreshToken(currentUsername, requestUri);

				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			}

		} else {
			throw new RuntimeException("missing refresh token");
		}
	}
}
