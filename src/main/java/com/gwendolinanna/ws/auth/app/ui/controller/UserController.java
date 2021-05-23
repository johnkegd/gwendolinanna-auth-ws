package com.gwendolinanna.ws.auth.app.ui.controller;

import com.gwendolinanna.ws.auth.app.exceptions.UserServiceException;
import com.gwendolinanna.ws.auth.app.service.PostService;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.request.UserDetailsRequestModel;
import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessages;
import com.gwendolinanna.ws.auth.app.ui.model.response.OperationStatusModel;
import com.gwendolinanna.ws.auth.app.ui.model.response.PostRest;
import com.gwendolinanna.ws.auth.app.ui.model.response.RequestOperationStatus;
import com.gwendolinanna.ws.auth.app.ui.model.response.UserRest;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johnkegd
 */
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    Utils utils;

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {

        UserDto userDto = userService.getUserByUserId(id);
        UserRest userRest = utils.getModelMapper().map(userDto, UserRest.class);

        return userRest;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = utils.getModelMapper().map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = utils.getModelMapper().map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserDto userDto = utils.getModelMapper().map(userDetails, UserDto.class);

        UserDto updateUser = userService.updateUserById(id, userDto);

        UserRest userRest = utils.getModelMapper().map(updateUser, UserRest.class);

        if (updateUser == null)
            throw new RuntimeException(ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());

        return userRest;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel operation = new OperationStatusModel();

        operation.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUserById(id);

        operation.setOperationResult(RequestOperationStatus.SUCCESS.name());

        //TODO implement operationData from deleted user

        return operation;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> users = new ArrayList<>();
        List<UserDto> usersDto = userService.getUsers(page,limit);

        for (UserDto user : usersDto) {
            UserRest userRest = utils.getModelMapper().map(user, UserRest.class);
            users.add(userRest);
        }

        return users;
    }

    @GetMapping(path = "/{id}/posts", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<PostRest> getPosts(@PathVariable String id) {
        List<PostRest> posts = new ArrayList<>();
        List<PostDto> postDto = postService.getPosts(id);

        if (postDto != null && !postDto.isEmpty()) {
            Type listType = new TypeToken<List<PostRest>>() {}.getType();
            posts = utils.getModelMapper().map(postDto, listType);
        }

        return posts;
    }

    @GetMapping(path = "/{userId}/posts/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PostRest getPost(@PathVariable String userId ,@PathVariable String postId) {
        PostDto postDto = postService.getPost(postId);
        PostRest postRest = utils.getModelMapper().map(postDto, PostRest.class);

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        postRest.add(userLink);

        return postRest;
    }

}
