package com.gwendolinanna.auth.ws.app.io.repositories;

import com.gwendolinanna.auth.ws.app.io.entity.RoleEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Johnkegd
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

}
