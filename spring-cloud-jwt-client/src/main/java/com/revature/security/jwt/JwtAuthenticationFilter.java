package com.revature.security.jwt;

import static com.revature.security.jwt.JwtConstants.HEADER_STRING;
import static com.revature.security.jwt.JwtConstants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revature.security.annotations.SpringCloudJwtComponent;

import io.jsonwebtoken.ExpiredJwtException;

@SpringCloudJwtComponent
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred while parsing Username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("Token is no longer valid", e);
            }
        } else {
            logger.info("Token not present in header. Will ignore request");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenUtil.validateToken(authToken, username)) {
                UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) jwtTokenUtil.generateAuthentication(authToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                System.err.println("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                final String token = JwtConstants.TOKEN_PREFIX + authToken;
                logger.info("Outgoing JWT Token: " + JwtConstants.TOKEN_PREFIX + authToken);
                res.addHeader(JwtConstants.HEADER_STRING, token);
            }
        }
        chain.doFilter(req, res);
    }
}
