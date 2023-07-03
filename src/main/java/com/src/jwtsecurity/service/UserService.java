package com.src.jwtsecurity.service;

import java.util.List;

import com.src.jwtsecurity.domain.Role;
import com.src.jwtsecurity.domain.User;

public interface UserService {

	User saveUser(User user);

	Role saveRole(Role role);

	void addRoleToUser(String username, String rolename);

	User getUser(String username);

	List<User> getUsers();
}
