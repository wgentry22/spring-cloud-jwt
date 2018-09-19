package com.revature.security.annotations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("spring.cloud.jwt.server")
public class SpringCloudJwtRegistrationProperties {

	private boolean enabled = true;
	private boolean autoRegisterEnabled = true;
	private String keystoreLocation;
	private String keystoreAlias;
	private String keystorePass;
	private boolean useGeneratedKeystore = false;
	private String generatedKeystoreAlgorithmType = "RSA";
	private int generatedKeystoreAlgorithmSize = 2048;
	
	public int getGeneratedKeystoreAlgorithmSize() {
		return generatedKeystoreAlgorithmSize;
	}

	public void setGeneratedKeystoreAlgorithmSize(int generatedKeystoreAlgorithmSize) {
		this.generatedKeystoreAlgorithmSize = generatedKeystoreAlgorithmSize;
	}

	public String getGeneratedKeystoreAlgorithmType() {
		return generatedKeystoreAlgorithmType;
	}

	public void setGeneratedKeystoreAlgorithmType(String generatedKeystoreAlgorithmType) {
		this.generatedKeystoreAlgorithmType = generatedKeystoreAlgorithmType;
	}

	public boolean getUseGeneratedKeystore() {
		return useGeneratedKeystore;
	}

	public void setUseGeneratedKeystore(boolean useGeneratedKeystore) {
		this.useGeneratedKeystore = useGeneratedKeystore;
	}

	public boolean isAutoRegisterEnabled() {
		return autoRegisterEnabled;
	}

	public void setAutoRegisterEnabled(boolean autoRegisterEnabled) {
		this.autoRegisterEnabled = autoRegisterEnabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getKeystoreLocation() {
		return keystoreLocation;
	}

	public void setKeystoreLocation(String keystoreLocation) {
		this.keystoreLocation = keystoreLocation;
	}

	public String getKeystoreAlias() {
		return keystoreAlias;
	}

	public void setKeystoreAlias(String keystoreAlias) {
		this.keystoreAlias = keystoreAlias;
	}

	public String getKeystorePass() {
		return keystorePass;
	}

	public void setKeystorePass(String keystorePass) {
		this.keystorePass = keystorePass;
	}
	
}
