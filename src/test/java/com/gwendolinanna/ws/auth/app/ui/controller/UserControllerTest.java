package com.gwendolinanna.ws.auth.app.ui.controller;

import com.gwendolinanna.ws.auth.app.service.impl.UserServiceImpl;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.response.UserRest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Johnkegd
 */
@SpringBootTest
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    Utils utils;


    private UserDto userDto;

    final String USER_ID = "kl123jñ2434";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Garcia");
        userDto.setEmail("test@gwendolinanna.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setPosts(getPostsDto());
        userDto.setEncryptedPassword("opqowjkdpoi");

    }

    @Test
    final void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);
        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertEquals(userDto.getEmail(), userRest.getEmail());
        assertEquals(userDto.getEmailVerificationStatus(), Boolean.FALSE);
        assertTrue(userDto.getPosts().size() == userRest.getPosts().size());

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
}
