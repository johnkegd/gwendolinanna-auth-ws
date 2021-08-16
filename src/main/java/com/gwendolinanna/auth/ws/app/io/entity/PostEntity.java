package com.gwendolinanna.auth.ws.app.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Getter
@Setter
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

}
