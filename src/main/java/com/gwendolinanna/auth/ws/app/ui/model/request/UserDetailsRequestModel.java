package com.gwendolinanna.auth.ws.app.ui.model.request;


import java.util.List;

/**
 * @author Johnkegd
 * @author GwendolinAnna
 */

public class UserDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profilePicture;
    private List<SocialMediaModel> socialMedia;
    private List<PostRequestModel> posts;


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

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<SocialMediaModel> getSocialMedia() {
        return socialMedia;
    }
    public void setSocialMedia(List<SocialMediaModel> socialMedia) {
        this.socialMedia = socialMedia;
    }

    public List<PostRequestModel> getPosts() {
        return posts;
    }
    public void setPosts(List<PostRequestModel> posts) {
        this.posts = posts;
    }
}
