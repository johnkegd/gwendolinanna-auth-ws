package com.gwendolinanna.ws.auth.app.io.repositories;

import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Johnkegd
 */

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);

}
