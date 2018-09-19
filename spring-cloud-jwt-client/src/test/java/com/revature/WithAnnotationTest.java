package com.revature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.security.annotations.EnableSpringCloudJwtClient;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.JwtTokenUtil;
import com.revature.security.jwt.KeyProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableSpringCloudJwtClient
public class WithAnnotationTest {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private KeyProvider keyProvider;

	@Test
	public void contextLoads_WithAnnotation() {}
	
	@Test
	public void autowiredBeans_shouldBeLoaded() {
		assert jwtTokenUtil != null;
		assert keyProvider != null;
		assert keyProvider instanceof GeneratedKeyProvider;
		assert keyProvider.getPublicKey() != null;
		assert keyProvider.getPrivateKey() != null;
	}

}
