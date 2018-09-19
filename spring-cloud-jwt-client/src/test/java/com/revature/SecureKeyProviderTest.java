package com.revature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtClient;
import com.revature.security.jwt.KeyProvider;
import com.revature.security.jwt.SecureKeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(
)
@EnableSpringCloudJwtClient
public class SecureKeyProviderTest {

	@Autowired
	private KeyProvider keyProvider;
	
	@Test
	public void keyProvider_ShouldBeSecureKeyProvider() {
		assert keyProvider != null;
		assert keyProvider instanceof SecureKeyProvider;
	}
	
	@Test
	public void keyProvider_ShouldPullProperties_FromConfigServer() {
		assert keyProvider.getPublicKey() != null;
		assert keyProvider.getPrivateKey() != null;
	}
}
