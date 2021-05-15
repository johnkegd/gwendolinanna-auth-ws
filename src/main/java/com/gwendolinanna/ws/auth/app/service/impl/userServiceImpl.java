package com.gwendolinanna.ws.auth.app.service.impl;

import com.gwendolinanna.ws.auth.app.exceptions.UserServiceException;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessage;
import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessages;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

/**
 * @author Johnkegd
 */
@Service
public class userServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("User already Exists");
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, userDto);


        return user;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity,userDto);

        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new RuntimeException(userId);

        BeanUtils.copyProperties(userEntity,userDto);
        return userDto;
    }

    @Override
    public UserDto updateUserById(String userId, UserDto user) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null || user.getFirstName().isEmpty() || user.getLastName().isEmpty())
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());

        //TODO separate method to password updates

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUserDetails,userDto);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw  new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }

}
