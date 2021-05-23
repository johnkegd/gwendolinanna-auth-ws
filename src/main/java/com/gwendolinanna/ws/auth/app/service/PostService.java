package com.gwendolinanna.ws.auth.app.service;

import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import java.util.List;

/**
 * @author Johnkegd
 */
public interface PostService {

    List<PostDto> getPosts(String userId);
}
