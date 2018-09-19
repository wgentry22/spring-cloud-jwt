package com.revature.security.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.revature.security.annotations.SpringCloudJwtComponent;
import com.revature.security.jwt.JwtConstants;

@SpringCloudJwtComponent
public class ZuulAuthenticationFilter extends ZuulFilter {
	
	private static Logger logger = LoggerFactory.getLogger(ZuulAuthenticationFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		final RequestContext context = RequestContext.getCurrentContext();
		System.err.println(context.getRequest().getMethod() + " request going to " + context.getRequest().getRequestURI());
		logger.info(context.getRequest().getMethod() + " request going to " + context.getRequest().getRequestURI());
		final String token = context.getRequest().getHeader(JwtConstants.HEADER_STRING) != null ? context.getRequest().getHeader(JwtConstants.HEADER_STRING) : context.getResponse().getHeader(JwtConstants.HEADER_STRING);
		context.getZuulRequestHeaders().put(JwtConstants.HEADER_STRING, token);
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
