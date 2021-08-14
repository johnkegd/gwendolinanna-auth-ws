package com.gwendolinanna.auth.ws.app.service.impl;

import com.gwendolinanna.auth.ws.app.io.entity.PostEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;
import com.gwendolinanna.auth.ws.app.io.repositories.PostRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.UserRepository;
import com.gwendolinanna.auth.ws.app.service.PostService;
import com.gwendolinanna.auth.ws.app.shared.Utils;
import com.gwendolinanna.auth.ws.app.shared.dto.PostDto;

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

        if (userEntity == null) {
            return postDtoList;
        }

        List<PostEntity> posts = postRepository.findAllByUserDetails(userEntity);
        for (PostEntity postEntity : posts) {
            postDtoList.add(utils.getModelMapper().map(postEntity, PostDto.class));
        }

        return postDtoList;
    }

    @Override
    public PostDto getPost(String postId) {
        PostEntity postEntity = postRepository.findByPostId(postId);

        if (postEntity != null) {
            return utils.getModelMapper().map(postEntity, PostDto.class);
        }

        return null;
    }
}
