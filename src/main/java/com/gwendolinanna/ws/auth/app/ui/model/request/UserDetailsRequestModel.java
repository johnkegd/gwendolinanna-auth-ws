package com.gwendolinanna.ws.auth.app.ui.model.request;


import java.util.List;

/**
 * @author Johnkegd
 */

public class UserDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<PostRequestModel> posts;

    public List<PostRequestModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostRequestModel> posts) {
        this.posts = posts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
