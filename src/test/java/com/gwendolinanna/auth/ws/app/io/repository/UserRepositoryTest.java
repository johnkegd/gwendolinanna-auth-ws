package com.gwendolinanna.auth.ws.app.io.repository;

import com.gwendolinanna.auth.ws.app.io.entity.PostEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;
import com.gwendolinanna.auth.ws.app.io.repositories.RoleRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.UserRepository;
import com.gwendolinanna.auth.ws.app.shared.Roles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Johnkegd
 */
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private static boolean recordsCreated = false;

    private static String USER_ID = "da123";

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
        String keyword = "Rotach";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
    }

    @Test
    final void testFindUsersFirstNameAndLastNameByKeyword() {
        String keyword = "Gwendolin";
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
        boolean newEmailVerificationStatus = true;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, USER_ID);

        UserEntity storedUserDetails = userRepository.findByUserId(USER_ID);

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    @Test
    final void testFindUserEntityByUserId() {
        String userId = "da123";
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));
    }

    @Test
    final void testGetUserEntityFullNameById() {
        List<Object[]> records = userRepository.getUserEntityFullNameById("da123");

        assertNotNull(records);
        assertTrue(records.size() == 1);
        String firstName = String.valueOf(records.get(0)[0]);
        String lastName = String.valueOf(records.get(0)[1]);
    }

    @Test
    final void testUpdateUserEntityEmailVerificationStatus() {
        boolean newStatus = true;
        userRepository.updateUserEntityEmailVerificationStatus(newStatus, USER_ID);

        boolean currentStatus = userRepository.findByUserId(USER_ID).getEmailVerificationStatus();

        assertTrue(currentStatus == newStatus);

    }

    private void createRecords() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Gwendolin");
        userEntity.setLastName("Rotach");
        userEntity.setUserId(USER_ID);
        userEntity.setEmail("gwendolin@gwendolinanna.com");
        userEntity.setEncryptedPassword("sdf1323");
        userEntity.setEmailVerificationStatus(true);
        userEntity.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName(Roles.ROLE_USER.name()))));

        PostEntity three = new PostEntity();
        three.setCategory("photography");
        three.setPostId("asda12e");
        three.setDescription("the best photos section from this gallery");
        three.setIcon("fas af-gallery");
        three.setIcon("image-tree.ong");
        three.setPrice(10);
        three.setTitle("the three");
        List<PostEntity> posts = new ArrayList<>();
        posts.add(three);

        userEntity.setPosts(posts);
        userRepository.save(userEntity);
        recordsCreated = true;
    }
}