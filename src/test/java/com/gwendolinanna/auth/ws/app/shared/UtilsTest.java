package com.gwendolinanna.auth.ws.app.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Johnkegd
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    private Utils utils;

    @BeforeEach
    public void setUp() throws Exception {

    }

    @Test
    final void testGeneratedUserId() {
        String userId = utils.generateUserId(30);
        String userId1 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId1);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId1));
    }

    @Test
    @Disabled
    final void testHasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("p123uj2ohf");
        assertNotNull(token);

        boolean hasTokenExpired = utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    @Disabled
    final void testHasTokenExpired() {
        String expiredToken = utils.generateEmailVerificationToken("12gh42ewfgryo5674werfq3");
        assertNotNull(expiredToken);

        boolean hasTokenExpired = utils.hasTokenExpired(expiredToken);
        assertTrue(hasTokenExpired);
    }

}