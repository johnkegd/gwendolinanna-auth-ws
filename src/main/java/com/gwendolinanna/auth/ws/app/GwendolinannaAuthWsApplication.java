package com.gwendolinanna.auth.ws.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GwendolinannaAuthWsApplication extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(GwendolinannaAuthWsApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GwendolinannaAuthWsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GwendolinannaAuthWsApplication.class, args);
    }


    @Bean
    ApplicationRunner applicationRunner(
            Environment environment,
            @Value("${hola-perinola:default message}") String hi) {

        return args -> {
            log.info("message: " + environment.getProperty("message-from-application-properties"));
            log.info("message 2: " + hi);
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }

}
