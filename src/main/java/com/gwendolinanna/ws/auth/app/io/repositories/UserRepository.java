package com.gwendolinanna.ws.auth.app.io.repositories;

import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select * from Users user where user.EMAIL_VERIFICATION_STATUS = 'true'",
            countQuery = "select count(*) from Users user where user.EMAIL_VERIFICATION_STATUS = 'true'",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmail(Pageable pageableRequest);

}
