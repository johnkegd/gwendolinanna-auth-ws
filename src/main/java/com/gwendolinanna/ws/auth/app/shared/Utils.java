package com.gwendolinanna.ws.auth.app.shared;

import com.gwendolinanna.ws.auth.app.security.SecurityConstants;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Johnkegd
 */
@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDFEHIJKLMNOPQRSTUVWXYZabcdfehijklmnopqrstuvwzyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;
    private ModelMapper modelMapper = new ModelMapper();

    public String generateUserId(int length) {
        return "user".concat(generateRandomString(length));
    }
    public String generatePostId(int length) {
        return "post".concat(generateRandomString(length));
    }

    private String generateRandomString(int length) {
        StringBuilder resultValue = new StringBuilder(length);

        for(int i=0; i < length; i++) {
            resultValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(resultValue);
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token).getBody();
        Date tokenExpirationDate = claims.getExpiration();
        Date todayDate = new Date();

        return tokenExpirationDate.before(todayDate);
    }
}
