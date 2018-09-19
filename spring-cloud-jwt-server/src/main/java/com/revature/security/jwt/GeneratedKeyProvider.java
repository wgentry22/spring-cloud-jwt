package com.revature.security.jwt;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.revature.security.annotations.SpringCloudJwtComponent;

@SpringCloudJwtComponent
@Scope("prototype")
public class GeneratedKeyProvider implements KeyProvider {
	
	@Autowired
	private GeneratedKeyProviderConstants generatedKeyProviderConstants;
	
	private KeyPair keyPair;

	public GeneratedKeyProvider() {
		super();
	}
	
	@Override
	public Key getPublicKey() {
		return keyPair.getPublic();
	}

	@Override
	public Key getPrivateKey() {
		return keyPair.getPrivate();
	}
	
	@PostConstruct
	public void setKeyPair() {
		assert generatedKeyProviderConstants.generatedKeyStoreAlgorithmSize != null;
		assert generatedKeyProviderConstants.generatedKeyStoreAlgorithmType != null;
		this.keyPair = generateKeyPair();
	}
	
	private KeyPair generateKeyPair() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance(generatedKeyProviderConstants.generatedKeyStoreAlgorithmType);
			generator.initialize(generatedKeyProviderConstants.generatedKeyStoreAlgorithmSize);
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
