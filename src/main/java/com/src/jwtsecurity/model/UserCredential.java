package com.src.jwtsecurity.model;

import java.io.Serializable;

public class UserCredential implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	public UserCredential() {

	}

	public UserCredential(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserCredential [username=" + username + ", password=" + password + "]";
	}

}
