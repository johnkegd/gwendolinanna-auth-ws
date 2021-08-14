package com.gwendolinanna.auth.ws.app.service;

import com.gwendolinanna.auth.ws.app.shared.dto.PostDto;

import java.util.List;

/**
 * @author Johnkegd
 */
public interface PostService {

    List<PostDto> getPosts(String userId);

    PostDto getPost(String postId);
}
