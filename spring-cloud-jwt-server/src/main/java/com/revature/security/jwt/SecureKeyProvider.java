package com.revature.security.jwt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Import;

import com.revature.security.annotations.SpringCloudJwtComponent;
import com.revature.security.annotations.config.SpringCloudJwtRegistrationConfiguration;

@SpringCloudJwtComponent
@Import(value=SpringCloudJwtRegistrationConfiguration.class)
@ConditionalOnProperty(name="spring.cloud.jwt.server.keystore-location")
@ConditionalOnMissingBean(value=GeneratedKeyProvider.class, search=SearchStrategy.ALL)
@Qualifier("secureKeyProvider")
public class SecureKeyProvider implements KeyProvider {
	
	@Autowired
	private JwtConstants jwtConstants;
	
	private String getKeyStoreLocation() {
		return this.jwtConstants.keystoreLocation;
	}
	
	private String getKeyStoreAlias() {
		return this.jwtConstants.keystoreAlias;
	}
	
	private String getKeyStorePassword() {
		return this.jwtConstants.keystorePass;
	}
	
	public Key getPublicKey() {
		try {
			return getKeyPair().getPublic();
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Key getPrivateKey() {
		try {
			return getKeyPair().getPrivate();
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private KeyPair getKeyPair() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		final InputStream resource = new FileInputStream(getKeyStoreLocation());
		final KeyStore keyStore = KeyStore.getInstance("jks");
		keyStore.load(resource, getKeyStorePassword().toCharArray());
		
		Key key = keyStore.getKey(getKeyStoreAlias(), getKeyStorePassword().toCharArray());
		if (key instanceof PrivateKey) {
			Certificate cert = keyStore.getCertificate(getKeyStoreAlias());
			PublicKey pubKey = cert.getPublicKey();
			return new KeyPair(pubKey, (PrivateKey) key);
		} else throw new IllegalStateException("Cannot load keys from store: " + resource);
	}
}
