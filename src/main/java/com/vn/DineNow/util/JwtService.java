package com.vn.DineNow.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vn.DineNow.payload.request.user.UserForGenerateToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("${DineNow.jwt.secret}")
    private String jwtSecret;

    @Value("${DineNow.jwt.expiration}")
    private long jwtExpiration;

    @Value("${DineNow.jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    @Value("${DineNow.jwt.audience}")
    private String audience;

    @Value("${DineNow.jwt.issuer}")
    private String issuer;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    public String generateAccessToken(UserForGenerateToken userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return JWT.create()
                .withSubject(Long.toString(userDetails.getId()))
                .withAudience(audience)
                .withIssuedAt(now)
                .withIssuer(issuer)
                .withExpiresAt(expiryDate)
                .withClaim("role", userDetails.getRole().getName())
                .sign(getAlgorithm());
    }

    public Long getUserIdFromJWT(String token) {
        DecodedJWT decodedJWT = JWT.require(getAlgorithm())
                .withIssuer(issuer)
                .build()
                .verify(token);
        return Long.parseLong(decodedJWT.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            JWT.require(getAlgorithm())
                    .withIssuer(issuer)
                    .build()
                    .verify(authToken);
            return true;
        } catch (Exception ex) {
            log.error("JWT validation error: {}", ex.getMessage());
        }
        return false;
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateRefreshToken(UserForGenerateToken userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);
        return JWT.create()
                .withSubject(Long.toString(userDetails.getId()))
                .withAudience(audience)
                .withIssuedAt(now)
                .withIssuer(issuer)
                .withExpiresAt(expiryDate)
                .withClaim("role", userDetails.getRole().getName())
                .sign(getAlgorithm());
    }
}
