package com.gwendolinanna.ws.auth.app.service;

import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Johnkegd
 */
public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUserByEmail(String email);
    UserDto getUserByUserId(String id);

}
