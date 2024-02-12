package com.sanhil.security;

import com.sanhil.service.userService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class jwtHelper {
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;

	private final String secretKey = "Sanhil";

	public  Integer getUseridfromToken(String token){
		return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
	}

	public Date getExpirationDateFromToken(String token){
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply((claims));
	}

	private Claims getAllClaimsFromToken(String token){
		Key signingKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token){
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(userService userservice){
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userservice.getId());
	}

	private String doGenerateToken(Map<String,Object> claims, Integer subject){
		Key signingKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
		return Jwts.builder()
									 .setClaims(claims)
									 .setSubject(String.valueOf(subject))
									 .setIssuedAt(new Date(System.currentTimeMillis()))
									 .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
									 .signWith(signingKey,SignatureAlgorithm.HS512)
									 .compact();
	}
	public Boolean validateToken(String token, userService userservice){
		final Integer userId = getUseridfromToken(token);
		return (userId.equals(userservice.getId()) && !isTokenExpired(token));
	}
}

