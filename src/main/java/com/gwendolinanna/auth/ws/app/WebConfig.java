package com.gwendolinanna.auth.ws.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Johnkegd
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                // .allowedMethods("GET","POST","PUT")
                .allowedMethods("*")
                // .allowedOrigins("https://gwendolinanna.com","http://localhost:8888")
                .allowedOrigins("*");
    }
}
