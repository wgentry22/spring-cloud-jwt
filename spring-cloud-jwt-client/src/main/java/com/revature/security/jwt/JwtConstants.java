package com.revature.security.jwt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.security.annotations.config.SpringCloudJwtRegistrationProperties;

@Component
public class JwtConstants {

	@Autowired
	private SpringCloudJwtRegistrationProperties.Server serverRegistrationProperties;
	
	public String keystoreLocation;
	
	public String keystoreAlias;
	
	public String keystorePass;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
	public static final long EXPIRATION = 3600; // 1 Hour
	
	@PostConstruct
	public void initProperties() {
		keystoreLocation = serverRegistrationProperties.getKeystoreLocation();
		keystoreAlias = serverRegistrationProperties.getKeystoreAlias();
		keystorePass = serverRegistrationProperties.getKeystorePass();
	}
}
