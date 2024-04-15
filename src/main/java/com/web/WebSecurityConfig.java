package com.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.web.config.AuthenticationSuccessHandler;
import com.web.config.MyUserDetailService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	MyUserDetailService myUserDetailService;
	
//	@Autowired
//	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return myUserDetailService;
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(myUserDetailService);
		provider.setPasswordEncoder(encoder());
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registory -> {
					registory.requestMatchers("/*").permitAll();
					registory.requestMatchers("/admin/**").hasRole("ADMIN");
					registory.requestMatchers("/user/**").hasRole("USER");
					registory.anyRequest().authenticated();
				})
				.formLogin(httpSecurityFormLoginConfigurer ->{
					httpSecurityFormLoginConfigurer
					.loginPage("/signing")
					.loginProcessingUrl("/userLogin")
					.usernameParameter("email")
					.successHandler(new AuthenticationSuccessHandler())
					.permitAll();
				})
				.build();
	}
	
}
