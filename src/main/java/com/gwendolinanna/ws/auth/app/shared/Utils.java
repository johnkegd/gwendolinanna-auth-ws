package com.gwendolinanna.ws.auth.app.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Johnkegd
 */
@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDFEHIJKLMNOPQRSTUVWXYZabcdfehijklmnopqrstuvwzyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder resultValue = new StringBuilder(length);

        for(int i=0; i < length; i++) {
            resultValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(resultValue);
    }

}
