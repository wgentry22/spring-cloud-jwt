package com.revature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.GeneratedKeyProviderConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(
			properties= {"spring.cloud.jwt.server.use-generated-keystore=true"}
		)
@EnableSpringCloudJwtServer
public class GeneratedKeyProviderConstantsTest {

	@Autowired
	private GeneratedKeyProviderConstants generatedKeyProviderConstants;
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void generatedKeyProviderConstantsShouldNotBeNull() {
		assertNotNull(generatedKeyProviderConstants);
	}
	
	@Test
	public void generatedKeyProviderConstantsShouldPullValuesFromConfigServer() {
		assertNotNull(generatedKeyProviderConstants.generatedKeyStoreAlgorithmSize);
		assertNotNull(generatedKeyProviderConstants.generatedKeyStoreAlgorithmType);
		assertNotNull(generatedKeyProviderConstants.useGeneratedKeyStore);
		
		assertEquals(generatedKeyProviderConstants.generatedKeyStoreAlgorithmSize, new Integer(2048));
		assertEquals(generatedKeyProviderConstants.generatedKeyStoreAlgorithmType, "RSA");
		assertTrue(generatedKeyProviderConstants.useGeneratedKeyStore);
	}
}
