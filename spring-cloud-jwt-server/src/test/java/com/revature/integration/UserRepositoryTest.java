package com.revature.integration;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.config.DataJpaTestConfig;
import com.revature.model.AuthenticatedUser;
import com.revature.model.Authorities;
import com.revature.repository.UserRepository;
import com.revature.security.JwtUserDetailsService;
import com.revature.security.annotations.EnableSpringCloudJwtServer;

@RunWith(SpringRunner.class)
@SpringBootTest(
		properties= {"spring.cloud.jwt.server.use-generated-keystore=true"}
)
@DataJpaTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes=DataJpaTestConfig.class)
@EnableSpringCloudJwtServer
public class UserRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private AuthenticatedUser generateAuthenticatedUser() {
		final AuthenticatedUser user = new AuthenticatedUser();
		user.setAuthorities(Arrays.asList(new Authorities("ROLE_ADMIN")).stream().collect(Collectors.toSet()));
		user.setUsername("william");
		user.setPassword(passwordEncoder.encode("password"));
		user.setEnabled(true);
		return user;
	}

	@Test
	public void contextLoads() {}
	
	@Test
	public void userDetailsServiceShouldNotBeNull() {
		assert userDetailsService != null;
	}
	
	@Test
	public void userRepositoryShouldNotBeNull() {
		assert userRepository != null;
	}
	
	@Test
	public void userRepositoryShouldFindByUsernameAndPassword() {
		testEntityManager.persist(generateAuthenticatedUser());
		assert passwordEncoder != null;
		final AuthenticatedUser testUser = this.userRepository.findOne("william");
		assert testUser != null;
		assert testUser.getUsername().equals("william") == true;
		assert passwordEncoder.matches("password", testUser.getPassword()) == true;
		assert testUser.getAuthorities().contains(new Authorities("ROLE_ADMIN"));
	}
	
	@Test
	public void jwtUserDetailsServiceShouldFindByUsernameAndPassword() {
		testEntityManager.persist(generateAuthenticatedUser());
		assert passwordEncoder != null;
		final UserDetails userDetails = this.userDetailsService.loadUserByUsernameAndPassword("william", "password");
		assert userDetails != null;
		assert userDetails.getUsername().equals("william") == true;
		assert userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		assert userDetails.isEnabled() == true;
		assert passwordEncoder.matches("password", userDetails.getPassword()) == true;
	}
	
}
