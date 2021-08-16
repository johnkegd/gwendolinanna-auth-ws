package com.gwendolinanna.auth.ws.app.io.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Entity(name = "password_reset_tokens")
@Getter
@Setter
public class PasswordResetTokenEntity implements Serializable {

    private static final long serialVersionUID = 6549985353725L;

    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @OneToOne()
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;


}
