package com.gwendolinanna.ws.auth.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwendolinanna.ws.auth.app.ui.model.request.UserLoginRequestModel;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Johnkegd
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

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
                                            Authentication authResult) throws IOException, ServletException {

        String userName = ((User) authResult.getPrincipal()).getUsername();
        //String tokenSecurity = new SecurityConstans().getTokenSecret();

        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstans.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstans.TOKEN_SECRET).compact();


       response.addHeader(SecurityConstans.HEADER_STRING, SecurityConstans.TOKEN_PREFIX + token);
    }
}
