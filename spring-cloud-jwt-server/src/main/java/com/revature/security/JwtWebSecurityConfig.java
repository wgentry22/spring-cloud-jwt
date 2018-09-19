package com.revature.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.revature.security.annotations.SpringCloudJwtServerConfiguration;
import com.revature.security.jwt.GeneratedKeyProvider;
import com.revature.security.jwt.GeneratedKeyProviderConstants;
import com.revature.security.jwt.JwtAuthenticationEntryPoint;
import com.revature.security.jwt.JwtAuthenticationFilter;
import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;
import com.revature.security.jwt.KeyProvider;
import com.revature.security.jwt.SecureKeyProvider;
import com.revature.security.zuul.ZuulAuthenticationFilter;

@SpringCloudJwtServerConfiguration
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * @author William Gentry
	 */
	
	@Autowired
	private GeneratedKeyProviderConstants generatedKeyProviderConstants;
	
	@Bean
	public JwtAuthenticationEntryPoint authenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}
	
	@Bean
	@Qualifier("jwtUserDetailsService")
	public JwtUserDetailsService jwtUserDetailsService() {
		return new JwtUserDetailsServiceImpl();
	}
	
	@Bean
	public ZuulAuthenticationFilter zuulAuthenticationFilter() {
		return new ZuulAuthenticationFilter();
	}
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(passwordEncoder(), jwtUserDetailsService());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider());
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
		
	}
	
	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(getAuthenticationSuccessHandler());
		filter.setAuthenticationFailureHandler(getAuthenticationFailureHandler());
		return filter;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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
	
	@Bean
	public CustomAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
	
	@Bean
	public CustomAuthenticationFailureHandler getAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	@Bean
	public CustomLogoutSuccessHandler getCustomLogoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
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
		
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http 
				.httpBasic().disable()
				.cors().configurationSource(corsConfigurationSource())
			.and().csrf().disable()
				.addFilterAfter(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
					.authenticationEntryPoint(authenticationEntryPoint())
					.accessDeniedHandler(customAccessDeniedHandler())
			.and()
				.formLogin()
				.permitAll()
			.and()
				.logout()
				.deleteCookies(new String[] {"JSESSIONID"})
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/bye-bye", HttpMethod.POST.toString()))
				.defaultLogoutSuccessHandlerFor(getCustomLogoutSuccessHandler(), new AntPathRequestMatcher("/bye-bye", HttpMethod.POST.toString()))
				.permitAll()
			.and()
				.sessionManagement()
				.maximumSessions(1)
			.and()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests().expressionHandler(webExpressionHandler()).anyRequest().fullyAuthenticated();
	}
	
}
