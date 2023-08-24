package com.xantrix.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("customUserDetailsService")
	CustomUserDetailsService customUserDetailsService;
	
	
	
	@Autowired
	// Attivazione del custom UserDetailsService
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(customUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
			super.configure(web);
			web.ignoring().antMatchers(HttpMethod.OPTIONS,"/**"); // FONDAMENTALE X ANGULAR
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	/*
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		
		UserBuilder users = User.builder();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(users
				.username("Carlo")
				.password(new BCryptPasswordEncoder().encode("123Stella"))
				.roles("USER").build());
		
		manager.createUser(users
				.username("Admin")
				.password(new BCryptPasswordEncoder().encode("123Stella"))
				.roles("USER","ADMIN").build());
		
		return manager;
	}*/
	
	private static final String[] USER_PATHS = {"/api/articoli/cerca/**,api/articoli/test/**"};
	private static final String[] ADMIN_PATHS = {"/api/articoli/inserisci/**","/api/articoli/modifica/**","/api/articoli/elimina/**"};
	private static final String REALM = "REAME";

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable()
		.authorizeHttpRequests()
			.antMatchers(USER_PATHS).hasRole("USER")
			.antMatchers(ADMIN_PATHS).hasRole("ADMIN")
		.and()
		.httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
	}

	


	@Bean 
	public AuthenticationEntryPoint getBasicAuthEntryPoint() {
		return new AuthEntryPoint();
	}
}
