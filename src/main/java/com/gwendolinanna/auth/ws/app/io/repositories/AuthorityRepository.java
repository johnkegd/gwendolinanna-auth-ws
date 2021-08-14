package com.gwendolinanna.auth.ws.app.io.repositories;

import com.gwendolinanna.auth.ws.app.io.entity.AuthorityEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Johnkegd
 */
@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    AuthorityEntity findByName(String name);

}
