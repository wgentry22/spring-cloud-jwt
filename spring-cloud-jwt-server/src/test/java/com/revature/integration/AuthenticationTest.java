package com.revature.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.config.DataJpaTestConfig;
import com.revature.model.AuthenticatedUser;
import com.revature.model.Authorities;
import com.revature.security.JwtUserDetailsService;
import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.output.ansi.enabled=ALWAYS" })
@ContextConfiguration(classes = DataJpaTestConfig.class)
@DataJpaTest
@AutoConfigureMockMvc(secure = false)
@AutoConfigureTestDatabase
@EnableSpringCloudJwtServer
public class AuthenticationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Test
	public void contextLoads() {
	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
								 .apply(springSecurity())
								 .build();
	}
	
	@Test
	public void postRequestToLoginProcessingUrl_WithInvalidCredentials_ShouldYieldUnauthorized() throws Exception {
		System.err.println("================= BEGIN POST TO LOGIN WITH INVALID CREDENTIALS =================");
		final String json = "{\"error\": \"Invalid Username/Password\"}";
		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("username", "someInvalidUsername").param("password", "someInvalidPassword"))
				.andExpect(status().is(401)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(json, true));
	}

	@Test
	@WithAnonymousUser
	public void postRequestToLoginProcessingUrl_WithValidCredentials_ShouldProduceAuthentication() throws Exception {
		System.err.println("================= BEGIN POST TO LOGIN WITH VALID CREDENTIALS =================");
		assert passwordEncoder != null;
		assert (passwordEncoder instanceof BCryptPasswordEncoder) == true;
		testEntityManager.persist(generateAuthenticatedUser());
		assert jwtUserDetailsService != null;
		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsernameAndPassword("william", "password");
		assert userDetails != null;
		assert userDetails.getUsername().equals("william") == true;
		assert passwordEncoder.matches("password", userDetails.getPassword()) == true;
		assert userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		mockMvc.perform(post("/authenticate").with(testSecurityContext()).param("username", "william").param("password", "password"))
			   .andExpect(status().isOk())
			   .andExpect(header().string(JwtConstants.HEADER_STRING, containsString(JwtConstants.TOKEN_PREFIX)))
			   .andExpect(authenticated().withSecurityContext(TestSecurityContextHolder.getContext()));
		assert TestSecurityContextHolder.getContext().getAuthentication() != null;
		final UserDetails principal = (UserDetails) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assert principal != null;
		assert principal.getUsername().equals("william");
		assert principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
		assert principal.isEnabled() && principal.isCredentialsNonExpired() && principal.isAccountNonLocked();
	} 

	@Test
	@WithMockUser(username="william", roles="ADMIN")
	public void postRequestToLoginProcessingUrl_ShouldLogout() throws Exception {
		System.err.println("================= BEGIN POST TO LOGOUT =================");
		assert TestSecurityContextHolder.getContext().getAuthentication() != null;
		mockMvc.perform(post("/bye-bye").headers(generateAuthenticatedHeaders()))
			   .andExpect(status().isOk())
			   .andExpect(unauthenticated());
		assert TestSecurityContextHolder.getContext().getAuthentication() == null;
	}
	

	private AuthenticatedUser generateAuthenticatedUser() {
		final AuthenticatedUser user = new AuthenticatedUser();
		user.setAuthorities(Arrays.asList(new Authorities("ROLE_ADMIN")).stream().collect(Collectors.toSet()));
		user.setUsername("william");
		user.setPassword(passwordEncoder.encode("password"));
		user.setEnabled(true);
		return user;
	}


	private HttpHeaders generateAuthenticatedHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtConstants.HEADER_STRING, JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(generateAuth()));
		return headers;
	}

	private UsernamePasswordAuthenticationToken generateAuth() {
		return new UsernamePasswordAuthenticationToken("william", passwordEncoder.encode("password"),
				Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}
}
