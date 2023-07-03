package com.src.jwtsecurity.api;

public class RoleUserForm {

	private String username;

	private String rolename;

	public RoleUserForm() {

	}

	public RoleUserForm(String username, String rolename) {
		setRolename(rolename);
		setUsername(username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
