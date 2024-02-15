package com.imfreepass.discord.user.config.jwt;

import java.time.Duration;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.imfreepass.discord.user.api.response.LoginUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class TokenProvider {
	
	private final JwtProperties jwtProperties;
	
	private String accessToken(Date expiry, LoginUser user) {
		Date now = new Date();
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer(jwtProperties.getIssuer())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.setSubject(user.getEmail())
				.claim("email", user.getEmail())
				.claim("nickname", user.getNickname())
				.claim("user_id", user.getUser_id())
				.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
				.compact();
	}
	private String tokenLink(Date expiry, String email, Long user_id) {
		Date now = new Date();
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer(jwtProperties.getIssuer())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.setSubject(email)
				.claim("email", email)
				.claim("user_id", user_id)
				.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
				.compact();
	}
	
	
	public String makeToken(LoginUser user, Duration expiredAt) {
		Date now = new Date();
		return accessToken(new Date(now.getTime() + expiredAt.toMillis()), user);
	}
	
	public String validateAndGetUserId(String token) throws JwtException{
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(jwtProperties.getSecretKey())
					.parseClaimsJws(token)
					.getBody();
			return claims.getSubject();
		} catch (JwtException e) {
			log.info("오류" + e);
			throw new JwtException("토큰 틀림", e);
		}
	}

	public String makeTokenLink(Long user_id,String email, Duration tokenTime) {
		Date now = new Date();
		return tokenLink(new Date(now.getTime() + tokenTime.toMillis()), email, user_id);
	}
	
}
