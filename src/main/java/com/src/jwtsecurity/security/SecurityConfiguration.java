package com.src.jwtsecurity.security;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.CACHE;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.EXECUTION_CONTEXTS;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.STORAGE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import com.src.jwtsecurity.auth.failure.AccessAuthorizationFailureHandler;
import com.src.jwtsecurity.auth.failure.LoginAuthenticationFailureHandler;
import com.src.jwtsecurity.constants.RequestUri;
import com.src.jwtsecurity.cypher.Cypher;
import com.src.jwtsecurity.jwtutils.JwtTokenUtils;
import com.src.jwtsecurity.logout.CustomLogoutHandler;
import com.src.jwtsecurity.logout.CustomLogoutSuccessHandler;
import com.src.jwtsecurity.security.filter.AuthenticationTokenFilter;
import com.src.jwtsecurity.security.filter.AuthenticationTokenSuccess;
import com.src.jwtsecurity.security.filter.AuthorizationTokenFilter;
import com.src.jwtsecurity.utils.HttpErrorResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final ClearSiteDataHeaderWriter.Directive[] SOURCE = { CACHE, COOKIES, STORAGE, EXECUTION_CONTEXTS };

	@Autowired
	@Qualifier("delegatedAuthenticationEntryPoint")
	private AuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private AuthorizationTokenFilter authTokenFilter;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtils jwtUtils;

	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private CustomLogoutHandler logoutHandler;

	@Autowired
	private HttpErrorResponse errorResponse;

	@Autowired
	private Cypher cypher;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationTokenFilter authFilter = new AuthenticationTokenFilter(authenticationManager, errorResponse);
		authFilter.setFilterProcessesUrl(RequestUri.LOGIN);
		authFilter.setAuthenticationSuccessHandler(authenticationTokenSuccess());
		authFilter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler());

		http.csrf().disable();
		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
		http.exceptionHandling().accessDeniedHandler(accessAuthorizationFailureHandler());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.logout(logout -> logout.logoutUrl(RequestUri.LOGOUT).addLogoutHandler(logoutHandler)
				.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(SOURCE)))
				.logoutSuccessHandler(logoutSuccessHandler).invalidateHttpSession(true).clearAuthentication(true));

		http.authorizeRequests().antMatchers(RequestUri.LOGIN, RequestUri.LOGOUT, RequestUri.REFRESH_TOKEN).permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/subject").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
			.antMatchers(HttpMethod.POST, "/api/subject/**").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/student/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
			
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(authFilter);
		http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AccessAuthorizationFailureHandler accessAuthorizationFailureHandler() {
		return new AccessAuthorizationFailureHandler(errorResponse);
	}

	@Bean
	public AuthenticationTokenSuccess authenticationTokenSuccess() {
		return new AuthenticationTokenSuccess(jwtUtils, cypher, errorResponse);
	}

	@Bean
	public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler() {
		return new LoginAuthenticationFailureHandler(errorResponse);
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
