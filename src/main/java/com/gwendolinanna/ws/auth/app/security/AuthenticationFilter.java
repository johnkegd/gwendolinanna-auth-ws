package com.gwendolinanna.ws.auth.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwendolinanna.ws.auth.app.SpringApplicationContext;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.request.UserLoginRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Johnkegd
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       try {
           UserLoginRequestModel credentials = new ObjectMapper().readValue(request.getInputStream(),UserLoginRequestModel.class);
           return authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           credentials.getEmail(),
                           credentials.getPassword(),
                           new ArrayList<>()));
       } catch (IOException e){
           throw new RuntimeException(e);
       }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        String userName = ((User) authResult.getPrincipal()).getUsername();
        //String tokenSecurity = new SecurityConstans().getTokenSecret();

        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = new UserDto();
        try {
            userDto = userService.getUserByEmail(userName);
        } catch (Exception e) {
            LOGGER.error("error auth", e);

        }


        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("UserID", userDto.getUserId());
    }
}
