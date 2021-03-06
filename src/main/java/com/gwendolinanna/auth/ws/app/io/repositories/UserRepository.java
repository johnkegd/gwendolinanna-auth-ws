package com.gwendolinanna.auth.ws.app.io.repositories;

import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Query(value = "select * from Users user where user.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "select * from Users user where user.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    @Query(value = "select * from Users user where user.first_name LIKE %:keyword% or user.last_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

    @Query(value = "select user.first_name, user.last_name from Users user where user.first_name LIKE %:keyword% or user.last_name LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUsersFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value = "update users user set user.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where user.user_id=:userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(
            @Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId);

    @Query("select user from UserEntity user where user.userId =:userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("select user.firstName, user.lastName from UserEntity user where user.userId =:userId")
    List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity user set user.emailVerificationStatus =:emailVerificationStatus where user.userId =:userId")
    void updateUserEntityEmailVerificationStatus(
            @Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId);
}
