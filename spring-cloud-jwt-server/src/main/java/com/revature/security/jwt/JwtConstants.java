package com.revature.security.jwt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.revature.security.annotations.config.SpringCloudJwtRegistrationProperties;

@Component
@ConditionalOnProperty(name="spring.cloud.jwt.server.use-generated-keystore", havingValue="false")
public class JwtConstants {
	
	/**
	 * @author William Gentry
	 */
	
	@Autowired
	private SpringCloudJwtRegistrationProperties springCloudJwtRegistrationProperties;
	
	public String keystoreLocation;
	
	public String keystoreAlias;
	
	public String keystorePass;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
	public static final long EXPIRATION = 3600; // 1 Hour
	
	@PostConstruct
	public void initProperties() {
		assert springCloudJwtRegistrationProperties != null;
		keystoreLocation = springCloudJwtRegistrationProperties.getKeystoreLocation();
		keystoreAlias = springCloudJwtRegistrationProperties.getKeystoreAlias();
		keystorePass = springCloudJwtRegistrationProperties.getKeystorePass();
	}
}
