package com.gwendolinanna.ws.auth.app.io.repository;

import com.gwendolinanna.ws.auth.app.io.entity.PostEntity;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Johnkegd
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Gwendolin");
        userEntity.setLastName("Rotach");
        userEntity.setUserId("da123");
        userEntity.setEmail("gwendolin@gwendolinanna.com");
        userEntity.setEncryptedPassword("sdf1323");
        userEntity.setEmailVerificationStatus(true);

        PostEntity three = new PostEntity();
        three.setCategory("photography");
        three.setPostId("asda12e");
        three.setDescription("the best photos section from this gallery");
        three.setIcon("fas af-gallery");
        three.setPrice(10);
        three.setTitle("the three");
        List<PostEntity> posts = new ArrayList<>();
        posts.add(three);

        userEntity.setPosts(posts);
        userRepository.save(userEntity);
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableReq = PageRequest.of(0, 2);
        Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmail(pageableReq);

        assertNotNull(page);
    }

}