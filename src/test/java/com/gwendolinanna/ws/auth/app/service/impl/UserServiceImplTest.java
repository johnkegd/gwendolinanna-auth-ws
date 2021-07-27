package com.gwendolinanna.ws.auth.app.service.impl;

import com.gwendolinanna.ws.auth.app.exceptions.UserServiceException;
import com.gwendolinanna.ws.auth.app.io.entity.PostEntity;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;
import com.gwendolinanna.ws.auth.app.shared.AmazonSES;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Johnkegd
 */
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    Utils utils;

    @Mock
    AmazonSES amazonSES;

    String userId = "cq24thqm7656";
    String encryptedPassword = "asfg23e123rg213";
    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void testGetUser() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setUserId("gasd123afa");
        userEntity.setEncryptedPassword("asgawer234sdg123");
        userEntity.setEmail("john@gwendolinanna.com");
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken("lu1s34ts1y31"));
        userEntity.setPosts(getPostsEntitty());

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUserByEmail("john@test.com");
        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UserNameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserByEmail("john@test.com");
        });
    }

    @Test
    final void TestGetUser_CreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = new UserDto();
        userDto.setPosts(getPostsDto());
        userDto.setFirstName("John");
        userDto.setLastName("Garcia");
        userDto.setPassword("22131");
        userDto.setEmail("john@gwendolinanna.com");

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    final void testCreateUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generatePostId(anyInt())).thenReturn("gasdqw1234ta12efg");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));


        UserDto userDto = new UserDto();
        userDto.setPosts(getPostsDto());

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getPosts().size(), userEntity.getPosts().size());
        verify(utils, times(2)).generatePostId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("654566");
        verify(userRepository, times(1)).save(any(UserEntity.class));

        userService.createUser(userDto);
    }

    private List<PostDto> getPostsDto() {
        PostDto three = new PostDto();
        three.setCategory("photography");
        three.setPostId(utils.generatePostId(10));
        three.setDescription("the best photos section from this gallery");
        three.setIcon("fas af-gallery");
        three.setPrice(10);
        three.setTitle("the three");

        PostDto woman = new PostDto();
        woman.setCategory("person");
        woman.setPostId(utils.generatePostId(15));
        woman.setDescription("a woman image");
        woman.setIcon("fas af-person");
        woman.setPrice(15);
        woman.setTitle("the woman");
        List<PostDto> posts = new ArrayList<>();
        posts.add(three);
        posts.add(woman);

        return posts;
    }

    private List<PostEntity> getPostsEntitty() {
        List<PostDto> posts = getPostsDto();
        Type listType = new TypeToken<List<PostEntity>>() {
        }.getType();
        return new ModelMapper().map(posts, listType);
    }

}
