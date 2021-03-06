package com.gwendolinanna.auth.ws.app.service.impl;

import com.gwendolinanna.auth.ws.app.exceptions.UserServiceException;
import com.gwendolinanna.auth.ws.app.io.entity.PasswordResetTokenEntity;
import com.gwendolinanna.auth.ws.app.io.entity.RoleEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;
import com.gwendolinanna.auth.ws.app.io.repositories.PasswordResetRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.RoleRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.UserRepository;
import com.gwendolinanna.auth.ws.app.security.UserPrincipal;
import com.gwendolinanna.auth.ws.app.service.UserService;
import com.gwendolinanna.auth.ws.app.shared.AmazonSES;
import com.gwendolinanna.auth.ws.app.shared.Utils;
import com.gwendolinanna.auth.ws.app.shared.dto.PostDto;
import com.gwendolinanna.auth.ws.app.shared.dto.UserDto;
import com.gwendolinanna.auth.ws.app.ui.model.response.ErrorMessages;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Johnkegd
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private AmazonSES amazonSES;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new UserServiceException("User already Exists");

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

        //setting roles
        Collection<RoleEntity> roleEntities = new HashSet<>();
        for (String role : user.getRoles()) {
            RoleEntity roleEntity = roleRepository.findByName(role);
            if (roleEntity != null) {
                roleEntities.add(roleEntity);
            }
        }

        userEntity.setRoles(new HashSet<>(roleEntities));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto userDto = utils.getModelMapper().map(storedUserDetails, UserDto.class);


        amazonSES.verifyEmail(userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        // UserDto userDto = new UserDto();
        // disabled because junit test failure
        // return utils.getModelMapper().map(userEntity, UserDto.class);
        //  BeanUtils.copyProperties(userEntity, userDto);

        // return userDto;
        return new ModelMapper().map(userEntity, UserDto.class);
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

        if (userEntity != null && !utils.hasTokenExpired(token)) {
            userEntity.setEmailVerificationToken(null);
            userEntity.setEmailVerificationStatus(Boolean.TRUE);
            userRepository.save(userEntity);
            verificationResult = true;
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

        if (passwordEntity != null) {
            passwordResetRepository.delete(passwordEntity);
        }
        return passwordRenewed;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new UserPrincipal(userEntity);

//        return new User(userEntity.getEmail(),
//                userEntity.getEncryptedPassword(),
//                userEntity.getEmailVerificationStatus(),
//                true, true, true,
//                new ArrayList<>());
    }

}
