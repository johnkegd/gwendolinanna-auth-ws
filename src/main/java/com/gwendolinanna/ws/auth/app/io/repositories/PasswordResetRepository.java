package com.gwendolinanna.ws.auth.app.io.repositories;

import com.gwendolinanna.ws.auth.app.io.entity.PasswordResetTokenEntity;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Johnkegd
 */
public interface PasswordResetRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
}
