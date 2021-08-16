package com.gwendolinanna.auth.ws.app.io.repositories;

import com.gwendolinanna.auth.ws.app.io.entity.PostEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Johnkegd
 */
@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {

    List<PostEntity> findAllByUserDetails(UserEntity userEntity);

    PostEntity findByPostId(String PostId);
}
