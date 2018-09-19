package com.revature.security.annotations.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.revature.security.jwt.JwtConstants;

@Configuration
@EnableConfigurationProperties(SpringCloudJwtRegistrationProperties.class)
public class SpringCloudJwtRegistrationConfiguration {
	
	@Bean
	public JwtConstants jwtConstants() {
		return new JwtConstants();
	}
}
