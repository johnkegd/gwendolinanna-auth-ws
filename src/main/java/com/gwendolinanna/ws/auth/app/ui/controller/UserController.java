package com.gwendolinanna.ws.auth.app.ui.controller;

import com.gwendolinanna.ws.auth.app.exceptions.UserServiceException;
import com.gwendolinanna.ws.auth.app.service.PostService;
import com.gwendolinanna.ws.auth.app.service.UserService;
import com.gwendolinanna.ws.auth.app.shared.Roles;
import com.gwendolinanna.ws.auth.app.shared.Utils;
import com.gwendolinanna.ws.auth.app.shared.dto.PostDto;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;
import com.gwendolinanna.ws.auth.app.ui.model.request.PasswordResetModel;
import com.gwendolinanna.ws.auth.app.ui.model.request.PasswordResetRequestModel;
import com.gwendolinanna.ws.auth.app.ui.model.request.UserDetailsRequestModel;
import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessages;
import com.gwendolinanna.ws.auth.app.ui.model.response.OperationStatusModel;
import com.gwendolinanna.ws.auth.app.ui.model.response.PostRest;
import com.gwendolinanna.ws.auth.app.ui.model.response.RequestOperationStatus;
import com.gwendolinanna.ws.auth.app.ui.model.response.UserRest;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author Johnkegd
 */
@RestController
@RequestMapping("users")
//@CrossOrigin(origins = {"https://gwendolinanna.com", "http://localhost:8888"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private Utils utils;

    @PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
    @ApiOperation(
            value = "Get user details web service endpoint",
            notes = "${userController.getUser.apiOperation.notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    //@CrossOrigin(origins = {"https://gwendolinanna.com", "http://localhost:8888"})
    public UserRest getUser(@PathVariable String id) {

        UserDto userDto = userService.getUserByUserId(id);
        UserRest userRest = utils.getModelMapper().map(userDto, UserRest.class);

        return userRest;
    }

    @ApiOperation(
            value = "Create and persist user details",
            notes = "${userController.createUser.apiOperation.notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = utils.getModelMapper().map(userDetails, UserDto.class);

        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = utils.getModelMapper().map(createdUser, UserRest.class);

        return returnValue;
    }

    @ApiOperation(
            value = "Update user details endpoint",
            notes = "${userController.updateUser.apiOperation.notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header"),
    })
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

    @PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
    //@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    //@Secured("ROLE_ADMIN")
    @ApiOperation(
            value = "Delete user details endpoint",
            notes = "${userController.deleteUser.apiOperation.notes}"

    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel operation = new OperationStatusModel();

        operation.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUserById(id);

        operation.setOperationResult(RequestOperationStatus.SUCCESS.name());

        //TODO implement operationData from deleted user

        return operation;
    }

    @ApiOperation(
            value = "Get (MULTIPLE) users details web service endpoint",
            notes = "${userController.getUsers.apiOperation.notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> users = new ArrayList<>();
        List<UserDto> usersDto = userService.getUsers(page, limit);

        Type listType = new TypeToken<List<UserRest>>() {
        }.getType();

        users = utils.getModelMapper().map(usersDto, listType);
//        for (UserDto user : usersDto) {
//            UserRest userRest = utils.getModelMapper().map(user, UserRest.class);
//            users.add(userRest);
//        }

        return users;
    }

    @ApiOperation(
            value = "Get user (MULTIPLE) posts details",
            notes = "${userController.getUserPosts.apiOperation.notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(path = "/{userId}/posts", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<PostRest> getUserPosts(@PathVariable String userId) {
        List<PostRest> posts = new ArrayList<>();
        List<PostDto> postDto = postService.getPosts(userId);

        if (postDto != null && !postDto.isEmpty()) {
            Type listType = new TypeToken<List<PostRest>>() {
            }.getType();
            posts = utils.getModelMapper().map(postDto, listType);

            for (PostRest postRest : posts) {
                Link postLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserPost(userId, postRest.getPostId())).withSelfRel();
                postRest.add(postLink);
            }

        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(UserController.class)
                        .getUserPosts(userId)).withSelfRel();

        return CollectionModel.of(posts, userLink, selfLink);
    }

    @ApiOperation(
            value = "Get user post details",
            notes = "${userController.getUserPost.apiOperation.notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(path = "/{userId}/posts/{postId}", produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
    })
    public EntityModel<PostRest> getUserPost(@PathVariable String userId, @PathVariable String postId) {
        PostDto postDto = postService.getPost(postId);
        PostRest postRest = utils.getModelMapper().map(postDto, PostRest.class);

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link userPostsLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(UserController.class)
                        .getUserPosts(userId)).withRel("posts");

        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(UserController.class).getUserPost(userId, postId)).withSelfRel();

        return EntityModel.of(postRest, Arrays.asList(userLink, userPostsLink, selfLink));
    }

    @GetMapping(path = "/email-verification",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
        OperationStatusModel operationStatus = new OperationStatusModel();
        operationStatus.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            operationStatus.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatus.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationStatus;
    }

    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel passwordResetRequest(@RequestBody PasswordResetRequestModel passwordResetRequest) {
        OperationStatusModel operationResponse = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequest.getEmail());
        operationResponse.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        operationResponse.setOperationData("Modified at ".concat(new Date(System.currentTimeMillis()).toString()));

        if (operationResult) {
            operationResponse.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationResponse.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationResponse;
    }

    @PostMapping(path = "/password-reset",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel operationStatus = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());
        operationStatus.setOperationName(RequestOperationName.PASSWORD_RESET.name());

        if (operationResult) {
            operationStatus.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatus.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationStatus;
    }
}
