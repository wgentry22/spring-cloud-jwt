package com.revature.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.revature.security.annotations.SpringCloudJwtClientConfiguration;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.GeneratedKeyProviderConstants;
import com.revature.security.jwt.JwtAuthenticationEntryPoint;
import com.revature.security.jwt.JwtAuthenticationFilter;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;
import com.revature.security.jwt.KeyProvider;
import com.revature.security.jwt.SecureKeyProvider;

@SpringCloudJwtClientConfiguration
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private GeneratedKeyProviderConstants generatedKeyProviderConstants;
	
	@Autowired
	private JwtConstants jwtConstants;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}
	
	@Bean
	public JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}
	
	@Bean
	public KeyProvider keyProvider() {
		if (generatedKeyProviderConstants.useGeneratedKeyStore) {
			if (generatedKeyProviderConstants.generatedKeyStoreAlgorithmType == null)
				throw new IllegalArgumentException("A value for spring.cloud.jwt.server.generated-keystore-algorithm-type must be provided");
			if (generatedKeyProviderConstants.generatedKeyStoreAlgorithmSize == null)
				throw new IllegalArgumentException("A value for spring.cloud.jwt.server.generated-keystore-algorithm-size must be provided");
			return new GeneratedKeyProvider();
		} else { 
			if (jwtConstants.keystoreLocation == null)
				throw new IllegalArgumentException("A value for spring.cloud.jwt.server.keystore-location must be provided");
			if (jwtConstants.keystoreAlias == null) 
				throw new IllegalArgumentException("A value for spring.cloud.jwt.server.keystore-alias must be provided");
			if (jwtConstants.keystorePass == null)
				throw new IllegalArgumentException("A value for spring.cloud.jwt.server.keystore-pass must be provided");
			return new SecureKeyProvider();
		}
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		// Customize any CORS requirements
		
		// Allow data from Config Server
		config.addAllowedOrigin("*");
		config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList(JwtConstants.HEADER_STRING));
		config.setExposedHeaders(Arrays.asList(JwtConstants.HEADER_STRING));
		UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource();
		// Set the CORS requirements
		url.registerCorsConfiguration("/**", config);
		return url;
	}
	
	/**
	 * Created a Hierarchy for Roles in the application
	 * Every ADMIN has access to ADMIN, MANAGER, and USER resources
	 * Every MANAGER has access to MANAGER and USER resources
	 * USER has access to USER resources only
	 */
	@Bean
	public RoleHierarchyImpl roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER and ROLE_MANAGER > ROLE_USER");
		return roleHierarchy;
	}
	
	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return defaultWebSecurityExpressionHandler;
	}
	
	@Bean
	public SimpleMappingExceptionResolver exceptionResolver() {
	    SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
	    exceptionResolver.setExcludedExceptions(AccessDeniedException.class);
	    return exceptionResolver;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http 
			.httpBasic().disable()
			.cors().configurationSource(corsConfigurationSource())
			.and().csrf().disable()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
					.authenticationEntryPoint(jwtAuthenticationEntryPoint())
					.accessDeniedHandler(customAccessDeniedHandler())
			.and()
				.authorizeRequests().expressionHandler(webExpressionHandler()).anyRequest().fullyAuthenticated();
	}
}
