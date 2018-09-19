package com.revature.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.security.annotations.EnableSpringCloudJwtServer;

@RunWith(SpringRunner.class)
@SpringBootTest(
		properties="spring.output.ansi.enabled=ALWAYS"
)
@AutoConfigureMockMvc
@EnableSpringCloudJwtServer
public class AuthorizationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	/**
	 * Derived from JwtAuthenticationEntryPoint
	 */
	final String anonymousJsonResponse = "{\"error\": \"Authorization required to view this resource\"}";
	
	/**
	 * Derived from CustomAccessDeniedHandler
	 */
	final String unauthorizedJsonResponse = "{\"error\": \"Improper Authorization\"}";
	
	/**
	 * Derived from AuthorityController (Simply a controller to perform these tests with)
	 */
	final String userJsonResponse = "{\"message\": \"Hello, User!\"}";
	final String managerJsonResponse = "{\"message\": \"Hello, Manager!\"}";
	final String adminJsonResponse = "{\"message\": \"Hello, Admin!\"}";
	
	@Before
	public void setUpMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
								 .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
								 .apply(springSecurity())
								 .build();
	}
	
	@Test
	@WithAnonymousUser
	public void anonymousUser_ShouldSeeUnauthorized_ForEveryPreAuthorizedEndpoint() throws Exception {
		mockMvc.perform(post("/user"))
				   .andExpect(status().isUnauthorized())
				   .andExpect(content().json(anonymousJsonResponse, true));
		mockMvc.perform(post("/manager"))
				   .andExpect(status().isUnauthorized())
				   .andExpect(content().json(anonymousJsonResponse, true));
		mockMvc.perform(post("/admin"))
				   .andExpect(status().isUnauthorized())
				   .andExpect(content().json(anonymousJsonResponse, true));
	}
	
	@Test
	@WithMockUser(roles="USER")
	public void roleUser_shouldSeeUserResource_andGetUnauthorized_AtManagerAndAdminEndpoints() throws Exception {
		mockMvc.perform(post("/user"))
			 		  .andExpect(status().isOk())
			 		  .andExpect(content().json(userJsonResponse, true));
		mockMvc.perform(post("/manager"))
		 			  .andExpect(status().isUnauthorized())
		 			  .andExpect(content().json(unauthorizedJsonResponse, true));
		mockMvc.perform(post("/admin"))
					  .andExpect(status().isUnauthorized())
					  .andExpect(content().json(unauthorizedJsonResponse, true));
	}
	
	@Test
	@WithMockUser(roles="MANAGER")
	public void roleManager_shouldSeeUserAndManagerResources_andGetUnauthorized_AtAdminEndpoint() throws Exception {
		mockMvc.perform(post("/user"))
			 		   .andExpect(status().isOk())
			 		   .andExpect(content().json(userJsonResponse, true));
		mockMvc.perform(post("/manager"))
					   .andExpect(status().isOk())
					   .andExpect(content().json(managerJsonResponse, true));
		mockMvc.perform(post("/admin"))
					   .andExpect(status().isUnauthorized())
					   .andExpect(content().json(unauthorizedJsonResponse, true));
	}
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void roleAdmin_shouldSeeAllResources_andNeverGetUnauthorized() throws Exception {
		mockMvc.perform(post("/user"))
				   .andExpect(status().isOk())
				   .andExpect(content().json(userJsonResponse, true));
		mockMvc.perform(post("/manager"))
				   .andExpect(status().isOk())
				   .andExpect(content().json(managerJsonResponse, true));
		mockMvc.perform(post("/admin"))
				   .andExpect(status().isOk())
				   .andExpect(content().json(adminJsonResponse, true));
	}
	
}
