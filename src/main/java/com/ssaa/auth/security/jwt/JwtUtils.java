package com.ssaa.auth.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ssaa.auth.config.JwtConfig;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private final JwtConfig jwtConfig;
	private final SecretKey key;

	public JwtUtils(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
		this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
	}

	public String generateToken(UserDetails userPrincipal) {
		return generateTokenFromUsername(userPrincipal.getUsername());
	}

	public String generateTokenFromUsername(String username) {
		Instant now = Instant.now();
		Instant expiration = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);

		return Jwts.builder().subject(username).issuedAt(Date.from(now)).expiration(Date.from(expiration))
				.signWith(key, Jwts.SIG.HS256).compact();
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("JWT validation error: {}", e.getMessage());
		}
		return false;
	}

	public Date getExpirationDateFromToken(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
	}

	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
}
