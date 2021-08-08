package com.gwendolinanna.ws.auth.app.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Setter
@Getter
@Entity
@Table(name = "authorities")
public class AuthorityEntity implements Serializable {

    private static final long serialVersionIUD = 9286712456757310L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;


    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;

    @Id
    public Long getId() {
        return id;
    }
}
