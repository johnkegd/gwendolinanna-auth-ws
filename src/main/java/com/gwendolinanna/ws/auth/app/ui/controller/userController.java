package com.gwendolinanna.ws.auth.app.ui.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Johnkegd
 */
@RestController
@RequestMapping("users")
public class userController {

    @GetMapping
    public String getUser() {
        return "get user called";
    }

    @PostMapping
    public String createUser() {
        return "create user called";
    }

    @PutMapping
    public String updateUser() {
        return "update user Called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user called";
    }
}
