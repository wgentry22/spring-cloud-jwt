package com.revature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtServer;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.KeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(
		properties= {"spring.cloud.jwt.server.use-generated-keystore=true"}
		)
@EnableSpringCloudJwtServer
public class GeneratedKeyProviderTest {

	@Autowired
	private KeyProvider keyProvider;
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void keyProviderShouldBeAutowired() {
		assertNotNull(keyProvider);
	}
	
	@Test
	public void keyProviderShouldBeGeneratedKeyProvider() {
		assertNotNull(keyProvider);
		assertTrue(keyProvider instanceof GeneratedKeyProvider);
	}
	
	@Test 
	public void keyProviderShouldGenerateKeys() {
		assertNotNull(keyProvider);
		assertNotNull(keyProvider.getPublicKey());
		assertNotNull(keyProvider.getPrivateKey());
	}
}
