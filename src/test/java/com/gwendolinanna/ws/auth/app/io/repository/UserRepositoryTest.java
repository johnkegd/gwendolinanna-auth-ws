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
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Johnkegd
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static boolean recordsCreated = false;

    @BeforeEach
    public void setUp() throws Exception {
        if (!recordsCreated) {
            createRecords();
        }
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableReq = PageRequest.of(0, 2);
        Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmail(pageableReq);

        List<UserEntity> userEntities = page.getContent();

        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 1);

    }

    @Test
    final void testfindUserByFirstName() {
        String firstName = "Gwendolin";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    final void testFindUserByLastName() {
        String lastName = "Rotach";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    final void testFindUserByKeyword() {
        String keyword = "wendo";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    final void testFindUsersFirstNameAndLastNameByKeyword() {
        String keyword = "tac";
        List<Object[]> users = userRepository.findUsersFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        Object[] user = users.get(0);
        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertTrue(userFirstName.contains(keyword) || userLastName.contains(keyword));
    }

    @Test
    final void testUpdateUserEmailVerificationStatus() {
        boolean newEmailVerification = false;
        userRepository.updateUserEmailVerificationStatus(newEmailVerification, "khjk12gbh");

        UserEntity storedUserDetails = userRepository.findByUserId("khjk12gbh");
        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerification);
    }

    private void createRecords() {
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
}