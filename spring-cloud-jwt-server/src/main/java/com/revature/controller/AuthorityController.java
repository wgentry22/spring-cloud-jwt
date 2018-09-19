package com.revature.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityController {

	@PostMapping(value="/admin", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Map<String, String> helloAdmin() {
		return Collections.singletonMap("message", "Hello, Admin!");
	}
	
	@PostMapping(value="/manager", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public Map<String, String> helloManager() {
		return Collections.singletonMap("message", "Hello, Manager!");
	}
	
	@PostMapping(value="/user", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public Map<String, String> helloUser() {
		return Collections.singletonMap("message", "Hello, User!");
	}
}
