package com.gwendolinanna.ws.auth.app.service.impl;

import com.gwendolinanna.ws.auth.app.UserRepository;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnkegd
 */
@Service
public class userServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId("testUserId");

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);


        return user;
    }

}
