package com.gwendolinanna.ws.auth.app.shared;

import org.modelmapper.ModelMapper;
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
}
