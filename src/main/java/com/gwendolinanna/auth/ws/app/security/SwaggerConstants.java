package com.gwendolinanna.auth.ws.app.security;

/**
 * @author Johnkegd
 */
public enum SwaggerConstants {
    API_PATH("/v2/api-docs"),
    CONFIG_PATH("/configuration/**"),
    SWAGGER_PATH("/swagger*/**"),
    WEBJARS_PATH("/webjars/**");

    private String pattern;

    SwaggerConstants(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }
}
