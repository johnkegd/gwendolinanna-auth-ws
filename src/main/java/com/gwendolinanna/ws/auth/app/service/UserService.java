package com.gwendolinanna.ws.auth.app.service;

import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Johnkegd
 */
public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    @Override
    UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException;
}
