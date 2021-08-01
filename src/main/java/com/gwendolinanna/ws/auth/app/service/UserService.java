package com.gwendolinanna.ws.auth.app.service;

import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author Johnkegd
 */
public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserId(String userId);

    UserDto updateUserById(String userId, UserDto user);

    void deleteUserById(String userId);

    List<UserDto> getUsers(int page, int limit);

    boolean verifyEmailToken(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);

}
