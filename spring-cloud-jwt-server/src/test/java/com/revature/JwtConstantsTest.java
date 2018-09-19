package com.revature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.JwtConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableSpringCloudJwtServer
public class JwtConstantsTest {

	@Autowired
	private JwtConstants jwtConstants;
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void jwtConstantsShouldNotBeNull() {
		assertNotNull(jwtConstants);
	}
	
	@Test
	public void jwtConstantsBeanShouldLoadPropertiesFromConfigServer() throws Exception {
		assertNotNull(jwtConstants.keystoreAlias);
		assertNotNull(jwtConstants.keystoreLocation);
		assertNotNull(jwtConstants.keystorePass);
		
		assertEquals(jwtConstants.keystoreAlias, "encrypt");
		assertEquals(jwtConstants.keystorePass, "revature");
		assertEquals(jwtConstants.keystoreLocation, "C:\\keys\\keytool\\encrypt.jks");
	}
}
