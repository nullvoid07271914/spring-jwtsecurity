package com.src.jwtsecurity.model;

import java.io.Serializable;
import java.util.Date;

public class RestError implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;

	private String message;

	private int code;

	private Date timestamp;

	public RestError() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "RestError [status=" + status + ", message=" + message + ", code=" + code + ", timestamp=" + timestamp
				+ "]";
	}

}
