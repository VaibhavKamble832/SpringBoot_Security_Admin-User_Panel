package com.web.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		System.out.println(role);
		
		if (role.contains("ROLE_ADMIN")) {
			System.out.println("admin loginn");
			response.sendRedirect("/admin/home");
		} 
		else {
			System.out.println("user loginn");
			response.sendRedirect("/user/home");
		}
	}
}
