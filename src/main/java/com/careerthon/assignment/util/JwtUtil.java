package com.careerthon.assignment.util;

import com.careerthon.assignment.exception.CustomJwtException;
import com.careerthon.assignment.exception.JwtExceptionMessage;
import com.careerthon.assignment.model.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic="jwtUtil")
@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${service.jwt.secret-key}")
    private String secretKey;
    private Key key;

    @Value("${service.jwt.expire-time}")
    private Long expireTime;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(UUID userId, String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .setExpiration(new Date(date.getTime() + expireTime))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm) // Key 객체를 사용
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

        } catch (SecurityException | MalformedJwtException | SignatureException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {

            log.warn(JwtExceptionMessage.INVALID_TOKEN.getMessage());
            throw new CustomJwtException(e.getMessage());

        }
    }
}
