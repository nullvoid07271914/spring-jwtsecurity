package com.src.jwtsecurity.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
	@PropertySource("classpath:error-props/error_response.properties"),
	@PropertySource("classpath:error-props/others.properties")
})
public class ErrorMessageProperties {

	public static String INTERNAL_SERVER_ERROR;

	public static String UNAUTHORIZED_ACCESS;

	public static String BAD_REQUEST;

	/*
	 * user.roles=user,admin
	 * 
	 * @Value("#{'${user.roles}'.split(',')}") private List<String>
	 * environmentsList;
	 */

	@Value("${bad.request}")
	public void setBadRequest(String message) {
		BAD_REQUEST = message;
	}

	@Value("${unauthorized.access}")
	public void setUnauthorizedAccess(String message) {
		UNAUTHORIZED_ACCESS = message;
	}

	@Value("${internal.server.error}")
	public void setInternalServerError(String message) {
		INTERNAL_SERVER_ERROR = message;
	}
}
