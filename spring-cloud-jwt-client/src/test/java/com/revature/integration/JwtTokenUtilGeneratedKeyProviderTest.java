package com.revature.integration;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtClient;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.JwtTokenUtil;
import com.revature.security.jwt.KeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(
		properties = { "spring.cloud.jwt.server.use-generated-keystore=true" }
)
@EnableSpringCloudJwtClient
public class JwtTokenUtilGeneratedKeyProviderTest {

	@Autowired
	private KeyProvider keyProvider;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Test
	public void jwtTokenUtil_ShouldGenerateToken_WithSecureKeyProvider() {
		assert jwtTokenUtil != null && jwtTokenUtil.generateToken(getAuthentication()) != null;
		assert keyProvider instanceof GeneratedKeyProvider;
	}
	
	
	@Test
	public void jwtTokenUtil_ShouldParseUserInformationProperly_FromAuthentication() {
		assert keyProvider instanceof GeneratedKeyProvider;
		final String token = jwtTokenUtil.generateToken(getAuthentication());
		assert token != null;
		final Authentication auth = jwtTokenUtil.generateAuthentication(token);
		assert auth != null;
		assert auth.getPrincipal().equals("william") == true;
		assert auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	@Test
	public void jwtTokenUtil_ShouldParseUserInformationProperly_FromUserDetails() {
		assert keyProvider instanceof GeneratedKeyProvider;
		final Authentication auth = jwtTokenUtil.generateAuthentication(getUserDetails());
		assert auth != null;
		assert auth.getPrincipal().equals("william") == true;
		assert auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	private UserDetails getUserDetails() {
		return User.withUsername("william").password("password").disabled(false).authorities("ROLE_ADMIN").build();
	}
	
	private Authentication getAuthentication() {
		return new UsernamePasswordAuthenticationToken("william", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}
}
