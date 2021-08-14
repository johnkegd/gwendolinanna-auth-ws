package com.gwendolinanna.auth.ws.app.security;

import com.gwendolinanna.auth.ws.app.io.entity.AuthorityEntity;
import com.gwendolinanna.auth.ws.app.io.entity.RoleEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Johnkegd
 */
public class UserPrincipal implements UserDetails {

    private static final long serialVersionIUD = -7912836110234358771L;

    private String userId;

    private UserEntity userEntity;

    public UserPrincipal() {
    }

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.userId = userEntity.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<AuthorityEntity> authorityEntities = new HashSet<>();

        // getting roles
        Collection<RoleEntity> roles = userEntity.getRoles();

        if (roles == null) {
            return authorities;
        } else {
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                authorityEntities.addAll(role.getAuthorities());
            });

            authorityEntities.forEach(authorityEntity -> {
                authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getEmailVerificationStatus();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
