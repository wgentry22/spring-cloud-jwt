package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.revature.security.annotations.EnableSpringCloudJwtClient;

@SpringBootApplication
@EnableFeignClients
@EnableSpringCloudJwtClient
public class SpringCloudJwtClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudJwtClientApplication.class, args);
	}
}
