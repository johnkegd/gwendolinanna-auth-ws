package com.gwendolinanna.auth.ws.app.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * @author Johnkegd
 * @author GwendolinAnna
 */
@Entity(name = "posts")
public class PostEntity implements Serializable {
    private static final long serialVersionUID = 6541231554545L;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 30, nullable = false)
    private String postId;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(length = 250)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String icon;
    private String category;
    private int price;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;


    public long GetId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
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
}
