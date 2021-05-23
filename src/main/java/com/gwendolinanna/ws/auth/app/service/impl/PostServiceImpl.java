package com.gwendolinanna.ws.auth.app.service.impl;

import com.gwendolinanna.ws.auth.app.io.entity.PostEntity;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.PostRepository;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;
import com.gwendolinanna.ws.auth.app.service.PostService;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johnkegd
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    Utils utils;

    @Override
    public List<PostDto> getPosts(String userId) {
        List<PostDto> postDtoList = new ArrayList<>();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null ) {
            return postDtoList;
        }

        List<PostEntity> posts = postRepository.findAllByUserDetails(userEntity);
        for (PostEntity postEntity : posts) {
            postDtoList.add(utils.getModelMapper().map(postEntity, PostDto.class));
        }

        return postDtoList;
    }
}
