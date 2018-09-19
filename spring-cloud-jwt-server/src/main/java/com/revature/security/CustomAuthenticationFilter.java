package com.revature.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.revature.security.annotations.SpringCloudJwtComponent;

@SpringCloudJwtComponent
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	
	@Autowired
	protected CustomAuthenticationProvider customAuthenticationProvider;
	
	public CustomAuthenticationFilter() {
		// Place in this constructor argument the URL and HTTP Method which processes your login
		super(new AntPathRequestMatcher("/authenticate", HttpMethod.POST));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		final UsernamePasswordAuthenticationToken token = getAuthRequest(request);
		return customAuthenticationProvider.authenticate(token);
	}
	
	// Used to get username and password parameters from login form
	protected UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return new UsernamePasswordAuthenticationToken(username , password);
	}
	
}
