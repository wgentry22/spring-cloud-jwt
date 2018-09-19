package com.revature.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.model.AuthenticatedUser;
import com.revature.repository.UserRepository;
import com.revature.security.annotations.SpringCloudJwtService;

@SpringCloudJwtService
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
		final AuthenticatedUser user = userRepository.findOne(username);
		if (user == null)
			throw new UsernameNotFoundException("Invalid Credentials");
		if (passwordEncoder.matches(password, user.getPassword())) {
			final UserDetails userDetails = User.withUsername(user.getUsername())
												.password(user.getPassword())
												.authorities(user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toList()))
												.disabled(!user.isEnabled())
												.build();
			return userDetails;
		} else throw new UsernameNotFoundException("Invalid Credentials");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final AuthenticatedUser user = userRepository.findOne(username);
		if (user == null)
			throw new UsernameNotFoundException("Invalid Credentials");
		final UserDetails userDetails = User.withUsername(user.getUsername())
											.password(user.getPassword())
											.authorities(user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toList()))
											.disabled(!user.isEnabled())
											.build();
		return userDetails;
	}

}
