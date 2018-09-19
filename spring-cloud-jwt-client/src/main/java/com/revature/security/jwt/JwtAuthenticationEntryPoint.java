package com.revature.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.security.annotations.SpringCloudJwtComponent;

@SpringCloudJwtComponent
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", "Authorization required to view this resource"));
		response.getOutputStream().write(body);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
