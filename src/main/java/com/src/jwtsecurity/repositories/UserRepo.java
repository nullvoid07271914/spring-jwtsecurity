package com.src.jwtsecurity.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.src.jwtsecurity.domain.User;
import com.src.jwtsecurity.query.Persistent;

@Repository
public interface UserRepo extends Persistent<User, Long> {

	@Query("SELECT e from User e WHERE e.username LIKE :username")
	User findRolesByUsername(@Param("username") String username);

	public User findByUsername(String username);

	public User findUserByUsername(String username);
}
