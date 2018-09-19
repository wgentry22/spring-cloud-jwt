package com.revature.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.security.annotations.SpringCloudJwtComponent;

@SpringCloudJwtComponent
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	/**
	 * @author William Gentry
	 */

	private PasswordEncoder passwordEncoder;
	private JwtUserDetailsService customUserDetailsService;
	
	public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, JwtUserDetailsService customUserDetailsService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException("Invalid Credentials");
		}
		
		String password = authentication.getCredentials().toString();
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Credentials"); 
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails authenticated = null;
		try {
			final UserDetails interim = this.customUserDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
			if (passwordEncoder.matches(authentication.getCredentials().toString(), interim.getPassword()));
				authenticated = this.customUserDetailsService.loadUserByUsernameAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
		} catch (UsernameNotFoundException userNotFound) {
			logger.info("Bad Credentials: ", userNotFound);
		}
		if (authenticated == null) {
            throw new InternalAuthenticationServiceException("JwtUserDetailsService failed to load Authentication");
        }
		return authenticated;
	}
	
	 

}
