package com.gwendolinanna.auth.ws.app.io.repositories;

import com.gwendolinanna.auth.ws.app.io.entity.PasswordResetTokenEntity;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Johnkegd
 */
public interface PasswordResetRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);
}
