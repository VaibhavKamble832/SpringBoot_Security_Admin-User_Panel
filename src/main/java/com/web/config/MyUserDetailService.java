package com.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.web.dto.MyUser;
import com.web.repository.MyUserRepo;

@Component
public class MyUserDetailService implements UserDetailsService{

	@Autowired
	MyUserRepo myUserRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUser myUser = myUserRepo.findByEmail(username);
		
		if (myUser == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		else {
			return new CustomUserDetails(myUser);
		}
	}
}
