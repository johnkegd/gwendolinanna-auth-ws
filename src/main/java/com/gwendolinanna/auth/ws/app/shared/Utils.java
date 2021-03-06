package com.gwendolinanna.auth.ws.app.shared;

import com.gwendolinanna.auth.ws.app.security.SecurityConstants;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Johnkegd
 */
@Service
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDFEHIJKLMNOPQRSTUVWXYZabcdfehijklmnopqrstuvwzyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;
    private ModelMapper modelMapper = new ModelMapper();

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generatePostId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder resultValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            resultValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(resultValue);
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public boolean hasTokenExpired(String token) {
        boolean expiredToken = false;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token).getBody();
            Date tokenExpirationDate = claims.getExpiration();

            Date todayDate = new Date();
            expiredToken = tokenExpirationDate.before(todayDate);
        } catch (ExpiredJwtException e) {
            expiredToken = true;
        }


        return expiredToken;
    }

    public String generateEmailVerificationToken(String userId) {
        return tokenGeneration(userId, SecurityConstants.EXPIRATION_TIME);
    }

    public String generatePasswordToken(String userId) {
        return tokenGeneration(userId, SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME);
    }

    private String tokenGeneration(String userId, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }
}
