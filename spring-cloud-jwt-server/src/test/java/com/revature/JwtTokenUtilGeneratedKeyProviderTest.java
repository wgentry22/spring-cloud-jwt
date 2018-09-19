package com.revature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.JwtTokenUtil;
import com.revature.security.jwt.KeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(
			properties = {"spring.cloud.jwt.server.use-generated-keystore=true"}
		)
@EnableSpringCloudJwtServer
public class JwtTokenUtilGeneratedKeyProviderTest {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private KeyProvider keyProvider;
	
	private final String username = "William";
	private final String password = "password";
	private final List<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	private final UserDetails userDetails = User.withUsername(username).password(password).authorities(authorities).disabled(false).build();
	private final Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void jwtTokenUtilShouldNotBeNull() {
		assertNotNull(jwtTokenUtil);
	}
	
	@Test
	public void jwtTokenUtilShouldGenerateToken() {
		assertNotNull(jwtTokenUtil.generateToken(auth));
	}
	
	@Test
	public void jwtTokenUtilShouldParseUserInfoFromToken() {
		assertNotNull(keyProvider);
		assertTrue(keyProvider instanceof GeneratedKeyProvider);
		final String token = jwtTokenUtil.generateToken(auth);
		assertNotNull(token);
		assertEquals(username, jwtTokenUtil.generateAuthentication(token).getPrincipal());
		assertEquals(authorities, jwtTokenUtil.generateAuthentication(token).getAuthorities());
	}
	
	@Test
	public void jwtTokenUtilShouldParseUserInfoFromUserDetails() {
		assertNotNull(keyProvider);
		assertTrue(keyProvider instanceof GeneratedKeyProvider);
		assertEquals(username, jwtTokenUtil.generateAuthentication(userDetails).getPrincipal());
		assertEquals(authorities, jwtTokenUtil.generateAuthentication(userDetails).getAuthorities());
	}
}
