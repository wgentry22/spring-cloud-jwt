package com.revature.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revature.security.annotations.SpringCloudJwtComponent;

import io.jsonwebtoken.Jwts;

@SpringCloudJwtComponent
public class JwtTokenUtil {
	
	/**
	 * @author William Gentry
	 */

	@Autowired
	private KeyProvider keyProvider;

	public String generateToken(final Authentication auth) {
		if (auth == null) {
			throw new IllegalArgumentException("Must provide valid AuthToken");
		}
		return Jwts.builder().setSubject(auth.getName())
				.claim(JwtConstants.AUTHORITIES_KEY,
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.collect(Collectors.joining(",")))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION * 1000))
				.signWith(keyProvider.getPrivateKey()).compact();
	}

	public Authentication generateAuthentication(final String token) {
		return new UsernamePasswordAuthenticationToken(getUsernameFromToken(token), "", getAuthoritiesFromToken(token));
	}

	public Authentication generateAuthentication(final UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "", userDetails.getAuthorities());
	}

	String getUsernameFromToken(final String token) {
		return Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody().getSubject();
	}

	Boolean validateToken(final String token, final UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	Boolean validateToken(final String token, final String username) {
		final String authenticatedUsername = getUsernameFromToken(token);
		return (username.equals(authenticatedUsername) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody()
				.getExpiration();
		return expiration.before(new Date());
	}

	private Collection<? extends GrantedAuthority> getAuthoritiesFromToken(final String token) {
		return Arrays
				.stream(Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody()
						.get(JwtConstants.AUTHORITIES_KEY).toString().split(","))
				.map(auth -> new SimpleGrantedAuthority(auth)).collect(Collectors.toSet());
	}
}
