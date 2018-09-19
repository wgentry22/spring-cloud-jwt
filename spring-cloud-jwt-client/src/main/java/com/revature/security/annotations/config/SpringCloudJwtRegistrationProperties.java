package com.revature.security.annotations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties
public class SpringCloudJwtRegistrationProperties {
	
	@Component("clientRegistrationProperties")
	@EnableConfigurationProperties
	@ConfigurationProperties("spring.cloud.jwt.client")
	public class Client {
		private boolean enabled = true;
		private boolean autoRegisterEnabled = true;
		
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public boolean isAutoRegisterEnabled() {
			return autoRegisterEnabled;
		}
		public void setAutoRegisterEnabled(boolean autoRegisterEnabled) {
			this.autoRegisterEnabled = autoRegisterEnabled;
		}
	}
	
	@Component("serverRegistrationProperties")
	@EnableConfigurationProperties
	@ConfigurationProperties("spring.cloud.jwt.server")
	public class Server {
		private boolean useGeneratedKeystore = false;
		private String keystoreLocation;
		private String keystoreAlias;
		private String keystorePass;
		private String generatedKeystoreAlgorithmType = "RSA";
		private int generatedKeystoreAlgorithmSize = 2048;
		
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

		public boolean isUseGeneratedKeystore() {
			return useGeneratedKeystore;
		}

		public void setUseGeneratedKeystore(boolean useGeneratedKeystore) {
			this.useGeneratedKeystore = useGeneratedKeystore;
		}

		public String getGeneratedKeystoreAlgorithmType() {
			return generatedKeystoreAlgorithmType;
		}

		public void setGeneratedKeystoreAlgorithmType(String generatedKeystoreAlgorithmType) {
			this.generatedKeystoreAlgorithmType = generatedKeystoreAlgorithmType;
		}

		public int getGeneratedKeystoreAlgorithmSize() {
			return generatedKeystoreAlgorithmSize;
		}

		public void setGeneratedKeystoreAlgorithmSize(int generatedKeystoreAlgorithmSize) {
			this.generatedKeystoreAlgorithmSize = generatedKeystoreAlgorithmSize;
		}
		
	}
}
