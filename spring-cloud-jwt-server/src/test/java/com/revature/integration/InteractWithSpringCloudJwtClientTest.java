package com.revature.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "spring.cloud.jwt.server.use-generated-keystore=false",
		"spring.output.ansi.enabled=ALWAYS" })
@AutoConfigureMockMvc
@EnableSpringCloudJwtServer
public class InteractWithSpringCloudJwtClientTest {
	
	/**
	 * This test will pass, however it requires that another service with @EnableSpringCloudJwtClient to be running
	 */
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private TestRestTemplate restTemplate;
	
	final String userResponse = "Hello from Spring Cloud Jwt Client, User!";
	final String managerResponse = "Hello from Spring Cloud Jwt Client, Manager!";
	final String adminResponse = "Hello from Spring Cloud Jwt Client, Admin!";
	final String unauthorizedResponse = "Authorization required to view this resource";
	final String springCloudJwtClientUrl = "http://localhost:7778";
	
	@Before
	public void setUpRestTemplate() throws IOException, URISyntaxException {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
		MockMvcClientHttpRequestFactory factory = new MockMvcClientHttpRequestFactory(mockMvc);
		RestTemplateBuilder builder = new RestTemplateBuilder();
		builder.requestFactory(factory);
		restTemplate = new TestRestTemplate(builder);
	}
	
	@Test
	@WithMockUser(roles="USER")
	public void roleUser_shouldSeeUserResource_andSeeUnauthorizedAtManagerAndAdminEndpoints() {
		assert TestSecurityContextHolder.getContext().getAuthentication() != null;
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(TestSecurityContextHolder.getContext().getAuthentication());
		assert token != null;
		final HttpHeaders headers = new HttpHeaders();
		headers.add(JwtConstants.HEADER_STRING, token);
		ResponseEntity<String> response = this.restTemplate.exchange(springCloudJwtClientUrl + "/user", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert response != null;
		assert response.getStatusCodeValue() == 200;
		assert response.getBody().equals(userResponse);
		
		ResponseEntity<String> secondResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/manager", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert secondResponse != null;
		assert secondResponse.getStatusCodeValue() == 401;
		System.err.println(secondResponse.getBody());
		assert secondResponse.getBody().equals(unauthorizedResponse);
		
		ResponseEntity<String> thirdResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/admin", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert thirdResponse != null;
		assert thirdResponse.getStatusCodeValue() == 401;
	}
	
	@Test
	@WithMockUser(roles="MANAGER")
	public void roleManager_ShouldSeeUserAndManagerResources_andSeeUnauthorizedAtAdminEndpoint() {
		assert TestSecurityContextHolder.getContext().getAuthentication() != null;
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(TestSecurityContextHolder.getContext().getAuthentication());
		assert token != null;
		final HttpHeaders headers = new HttpHeaders();
		headers.add(JwtConstants.HEADER_STRING, token);
		ResponseEntity<String> response = this.restTemplate.exchange(springCloudJwtClientUrl + "/user", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert response != null;
		assert response.getStatusCodeValue() == 200;
		assert response.getBody().equals(userResponse);
		
		ResponseEntity<String> secondResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/manager", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert secondResponse != null;
		assert secondResponse.getStatusCodeValue() == 200;
		assert secondResponse.getBody().equals(managerResponse);
		
		ResponseEntity<String> thirdResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/admin", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert thirdResponse != null;
		assert thirdResponse.getStatusCodeValue() == 401;
	}
	
	@Test 
	@WithMockUser(roles="ADMIN")
	public void roleAdmin_ShouldSeeAllResources() {
		assert TestSecurityContextHolder.getContext().getAuthentication() != null;
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(TestSecurityContextHolder.getContext().getAuthentication());
		assert token != null;
		final HttpHeaders headers = new HttpHeaders();
		headers.add(JwtConstants.HEADER_STRING, token);
		ResponseEntity<String> response = this.restTemplate.exchange(springCloudJwtClientUrl + "/user", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert response != null;
		assert response.getStatusCodeValue() == 200;
		assert response.getBody().equals(userResponse);
		
		ResponseEntity<String> secondResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/manager", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert secondResponse != null;
		assert secondResponse.getStatusCodeValue() == 200;
		assert secondResponse.getBody().equals(managerResponse);
		
		ResponseEntity<String> thirdResponse = this.restTemplate.exchange(springCloudJwtClientUrl + "/admin", HttpMethod.POST, new HttpEntity<>(headers), String.class);
		assert thirdResponse != null;
		assert thirdResponse.getStatusCodeValue() == 200;
		assert thirdResponse.getBody().equals(adminResponse);
	}
}
