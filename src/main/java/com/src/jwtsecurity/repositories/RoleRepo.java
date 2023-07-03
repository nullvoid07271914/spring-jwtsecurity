package com.src.jwtsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.jwtsecurity.domain.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

	Role findByName(String name);
}
