package com.revature.security.annotations.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.revature.security.jwt.GeneratedKeyProviderConstants;
import com.revature.security.jwt.JwtConstants;

@Configuration
@EnableConfigurationProperties({SpringCloudJwtRegistrationProperties.Server.class, SpringCloudJwtRegistrationProperties.Client.class})
public class SpringCloudJwtRegistrationConfiguration {
	
	@Bean
	public JwtConstants jwtConstants() {
		return new JwtConstants();
	}
	
	@Bean
	public GeneratedKeyProviderConstants generatedKeyProviderConstants() {
		return new GeneratedKeyProviderConstants();
	}
}
