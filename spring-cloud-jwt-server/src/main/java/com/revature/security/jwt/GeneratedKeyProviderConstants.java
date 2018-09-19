package com.revature.security.jwt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.security.annotations.config.SpringCloudJwtRegistrationProperties;

@Component
public class GeneratedKeyProviderConstants {
	
	@Autowired
	private SpringCloudJwtRegistrationProperties springCloudJwtRegistrationProperties;
	
	public Boolean useGeneratedKeyStore;
	public String generatedKeyStoreAlgorithmType;
	public Integer generatedKeyStoreAlgorithmSize;
	
	
	@PostConstruct
	public void initProperties() {
		assert springCloudJwtRegistrationProperties != null;
		useGeneratedKeyStore = springCloudJwtRegistrationProperties.getUseGeneratedKeystore();
		generatedKeyStoreAlgorithmType = springCloudJwtRegistrationProperties.getGeneratedKeystoreAlgorithmType();
		generatedKeyStoreAlgorithmSize = springCloudJwtRegistrationProperties.getGeneratedKeystoreAlgorithmSize();
	}
}
