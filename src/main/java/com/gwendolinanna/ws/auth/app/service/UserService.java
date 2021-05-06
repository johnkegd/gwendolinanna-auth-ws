package com.gwendolinanna.ws.auth.app.service;

import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

/**
 * @author Johnkegd
 */
public interface UserService {

    UserDto createUser(UserDto user);

}
