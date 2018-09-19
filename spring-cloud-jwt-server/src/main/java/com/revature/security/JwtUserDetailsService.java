package com.revature.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface JwtUserDetailsService extends UserDetailsService {
	org.springframework.security.core.userdetails.UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException;
	org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
