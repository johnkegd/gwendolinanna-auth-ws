package com.gwendolinanna.ws.auth.app.service.impl;

import com.gwendolinanna.ws.auth.app.exceptions.UserServiceException;
import com.gwendolinanna.ws.auth.app.io.entity.PasswordResetTokenEntity;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.PasswordResetRepository;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.AmazonSES;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

/**
 * @author Johnkegd
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private Utils utils;


    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("User already Exists");

        for (PostDto post : user.getPosts()) {
            post.setPostId(utils.generatePostId(20));
            post.setUserDetails(user);
        }


        UserEntity userEntity = utils.getModelMapper().map(user, UserEntity.class);
        String publicUserId = utils.generateUserId(30);

        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto userDto = utils.getModelMapper().map(storedUserDetails, UserDto.class);


        new AmazonSES().verifiyEmail(userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);
        UserDto userDto = null;
        try {
            userDto = utils.getModelMapper().map(userEntity, UserDto.class);
        } catch (Exception e) {
            LOGGER.error("UserService getUserByEmail error:", e);
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new RuntimeException(userId);

        UserDto userDto = utils.getModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto updateUserById(String userId, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null || user.getFirstName().isEmpty() || user.getLastName().isEmpty())
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());

        //TODO separate method to password updates

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);
        UserDto userDto = utils.getModelMapper().map(updatedUserDetails, UserDto.class);

        return userDto;
    }

    @Transactional
    @Override
    public void deleteUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> usersDto = new ArrayList<>();

        if (page > 0) page--;

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = utils.getModelMapper().map(userEntity, UserDto.class);
            usersDto.add(userDto);
        }

        return usersDto;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean verificationResult = false;
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            if (!utils.hasTokenExpired(token)) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                verificationResult = true;
            }

        }

        return verificationResult;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity != null) {
            String token = utils.generatePasswordToken(userEntity.getUserId());
            PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
            passwordResetTokenEntity.setToken(token);
            passwordResetTokenEntity.setUserDetails(userEntity);
            passwordResetRepository.save(passwordResetTokenEntity);
            //skip amaxon SES
            return true;
            //return AmazonSES.sendPasswordResetRequest(new UserDto(userEntity.getFirstName(), userEntity.getEmail(), token));
        }

        return false;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean passwordRenewed = false;
        PasswordResetTokenEntity passwordEntity = passwordResetRepository.findByToken(token);
        if (!utils.hasTokenExpired(token) && passwordEntity != null) {
            UserEntity userEntity = passwordEntity.getUserDetails();
            userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
            UserEntity savedEntity = userRepository.save(userEntity);
            if (savedEntity != null && savedEntity.getEncryptedPassword().equalsIgnoreCase(userEntity.getEncryptedPassword())) {
                passwordRenewed = true;
            }
        }
        passwordResetRepository.delete(passwordEntity);
        return passwordRenewed;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);


        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                userEntity.getEmailVerificationStatus(),
                true, true, true,
                new ArrayList<>());
    }

}
