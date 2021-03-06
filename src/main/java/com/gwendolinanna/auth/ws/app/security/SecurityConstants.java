package com.gwendolinanna.auth.ws.app.security;

import com.gwendolinanna.auth.ws.app.SpringApplicationContext;

/**
 * @author Johnkegd
 */
public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SING_UP_URL = "/users";
    public static final String AUTH_LOGIN_URL = "/users/login";
    public static final String VERIFICATION_EMAIL_URL = SING_UP_URL.concat("/email-verification");
    public static final String PASSWORD_RESET_REQUEST_URL = SING_UP_URL.concat("/password-reset-request");
    public static final String PASSWORD_RESET_URL = SING_UP_URL.concat("/password-reset");
    public static final String H2_CONSOLE = "/h2-console/**";

    public static final String READ_AUTHORITY = "READ_AUTHORITY";
    public static final String WRITE_AUTHORITY = "WRITE_AUTHORITY";
    public static final String DELETE_AUTHORITY = "DELETE_AUTHORITY";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }

}
