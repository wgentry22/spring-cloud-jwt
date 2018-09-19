package com.revature.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("spring-cloud-jwt-client")
public interface JwtFrameworkService {

	@PostMapping(value="/admin", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> getAdminMessage();
	
	@PostMapping(value="/manager", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public ResponseEntity<String> getManagerMessage();
	
	@PostMapping(value="/user", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<String> getUserMessage();
}
