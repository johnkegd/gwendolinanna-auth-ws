package com.gwendolinanna.auth.ws.app.ui.controller;

import com.gwendolinanna.auth.ws.app.ui.model.request.UserLoginRequestModel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

/**
 * @author Johnkegd
 */
@RestController
public class AuthenticationController {

    @ApiOperation("User login")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Response Headers",
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "authorization",
                                    description = "Barer <JWT value here>",
                                    response = String.class),
                            @ResponseHeader(
                                    name = "userId",
                                    description = "<Public User Id value here>",
                                    response = String.class)
                    })
    })
    @PostMapping("/users/login")
    public void fakeLogin(@RequestBody UserLoginRequestModel UserLoginRequestModel) {
        throw new IllegalStateException("This method should no be called. it's implemented by Spring Security");
    }

}
