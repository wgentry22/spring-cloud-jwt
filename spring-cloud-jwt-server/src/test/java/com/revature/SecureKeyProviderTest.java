package com.revature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.KeyProvider;
import com.revature.security.jwt.SecureKeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(
			properties= {"spring.cloud.jwt.server.use-generated-keystore=false"}
		)
@EnableSpringCloudJwtServer
public class SecureKeyProviderTest {

	@Autowired
	private KeyProvider keyProvider;
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void keyProviderShouldNotBeNull() {
		assertNotNull(keyProvider);
	}
	
	@Test
	public void keyProviderShouldBeSecureKeyProvider() {
		assertTrue(keyProvider instanceof SecureKeyProvider);
	}
	
	@Test
	public void keyProviderShouldGenerateKeys() {
		assertNotNull(keyProvider);
		assertNotNull(keyProvider.getPublicKey());
		assertNotNull(keyProvider.getPrivateKey());
	}
}
