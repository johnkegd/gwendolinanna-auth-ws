package com.gwendolinanna.ws.auth.app.io.repositories;

import com.gwendolinanna.ws.auth.app.io.entity.RoleEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Johnkegd
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
    
}
