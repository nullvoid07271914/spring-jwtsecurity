package com.src.jwtsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.src.jwtsecurity.service.UserService;

@SpringBootApplication
@ComponentScan
public class JwtsecurityApplication {

	@Autowired
	UserService repo;

	public static void main(String[] args) {
		SpringApplication.run(JwtsecurityApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public CommandLineRunner demo() {
//		return (args) -> {
//			Role role = new Role("ROLE_ADMIN");
//			repo.saveRole(role);
//
//			List<Role> roles = new ArrayList<Role>();
//			roles.add(role);
//
//			User user = new User();
//			user.setName("prince");
//			user.setUsername("username");
//			user.setPassword("password");
//			user.setRoles(roles);
//
//			repo.saveUser(user);
//		};
//	}
}
