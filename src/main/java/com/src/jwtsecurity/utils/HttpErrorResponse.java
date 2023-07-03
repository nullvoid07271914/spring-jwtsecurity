package com.src.jwtsecurity.utils;

import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.jwtsecurity.model.RestError;
import com.src.jwtsecurity.properties.ErrorMessageProperties;
import com.src.jwtsecurity.security.filter.AuthorizationTokenFilter;

@Service
public class HttpErrorResponse {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationTokenFilter.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ErrorMessageProperties errorMessage;

	public void response(HttpServletResponse response, int status) {
		try {
			String errorMessage = createErrorMessage(status);
			OutputStream responseStream = response.getOutputStream();
			RestError error = new RestError();
			error.setMessage(errorMessage);
			error.setStatus(HttpStatus.valueOf(status).getReasonPhrase());
			error.setCode(status);
			error.setTimestamp(new Date());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(status);
			mapper.writeValue(responseStream, error);
			responseStream.flush();
		} catch (Exception e) {
			logger.error("Exception: {}", e);
		}
	}

	public RestError createError(int status) {
		RestError error = new RestError();
		error.setMessage(createErrorMessage(status));
		error.setStatus(HttpStatus.valueOf(status).getReasonPhrase());
		error.setCode(status);
		error.setTimestamp(new Date());
		return error;
	}

	@SuppressWarnings("static-access")
	private String createErrorMessage(int status) {
		String message = "";
		switch (status) {
			case 400:
				message = errorMessage.BAD_REQUEST;
				break;
			case 401:
				message = errorMessage.UNAUTHORIZED_ACCESS;
				break;
			case 500:
				message = errorMessage.INTERNAL_SERVER_ERROR;
				break;
			default:
				break;
		}
		return message;
	}
}
