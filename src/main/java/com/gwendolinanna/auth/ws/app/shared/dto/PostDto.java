package com.gwendolinanna.auth.ws.app.shared.dto;

import java.io.Serializable;

/**
 * @author Johnkegd
 */
public class PostDto implements Serializable {

    private static final long serialVersionUID = 6546543515564L;
    private long id;
    private String postId;
    private String title;
    private String description;
    private String image;
    private String icon;
    private String category;
    private int price;
    private UserDto userDetails;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UserDto getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDto userDetails) {
        this.userDetails = userDetails;
    }
}
