package com.revature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.revature.service.JwtFrameworkService;

@RestController
public class JwtFrameworkController implements JwtFrameworkService {

	@Override
	public ResponseEntity<String> getAdminMessage() {
		return ResponseEntity.ok("Hello from Spring Cloud Jwt Client, Admin!");
	}

	@Override
	public ResponseEntity<String> getManagerMessage() {
		return ResponseEntity.ok("Hello from Spring Cloud Jwt Client, Manager!");
	}

	@Override
	public ResponseEntity<String> getUserMessage() {
		return ResponseEntity.ok("Hello from Spring Cloud Jwt Client, User!");
	}

}
