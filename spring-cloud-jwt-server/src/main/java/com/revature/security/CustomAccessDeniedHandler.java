package com.revature.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.security.annotations.SpringCloudJwtComponent;

@SpringCloudJwtComponent
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", "Improper Authorization"));
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getOutputStream().write(body);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
