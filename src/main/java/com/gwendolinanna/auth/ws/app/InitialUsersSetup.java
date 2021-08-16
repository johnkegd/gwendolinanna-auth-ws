package com.gwendolinanna.auth.ws.app;

import com.gwendolinanna.auth.ws.app.io.entity.AuthorityEntity;
import com.gwendolinanna.auth.ws.app.io.entity.RoleEntity;
import com.gwendolinanna.auth.ws.app.io.entity.UserEntity;
import com.gwendolinanna.auth.ws.app.io.repositories.AuthorityRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.RoleRepository;
import com.gwendolinanna.auth.ws.app.io.repositories.UserRepository;
import com.gwendolinanna.auth.ws.app.shared.Roles;
import com.gwendolinanna.auth.ws.app.shared.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static com.gwendolinanna.auth.ws.app.security.SecurityConstants.DELETE_AUTHORITY;
import static com.gwendolinanna.auth.ws.app.security.SecurityConstants.READ_AUTHORITY;
import static com.gwendolinanna.auth.ws.app.security.SecurityConstants.WRITE_AUTHORITY;

/**
 * @author Johnkegd
 */
@Component
public class InitialUsersSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Utils utils;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready");

        AuthorityEntity readAuthority = createAuthority(READ_AUTHORITY);
        AuthorityEntity writeAuthority = createAuthority(WRITE_AUTHORITY);
        AuthorityEntity deleteAuthority = createAuthority(DELETE_AUTHORITY);

        RoleEntity roleUser = createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        UserEntity user = new UserEntity();
        if (roleAdmin == null) return;


        user.setFirstName("John");
        user.setLastName("Garcia");
        user.setEmail("john@gwendolinanna.com");
        user.setEmailVerificationStatus(true);
        user.setUserId(utils.generateUserId(10));
        user.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        user.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));


        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity != null) {
            userRepository.delete(userEntity);
        }
        userRepository.save(user);

    }

    @Transactional
    protected AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);

        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }

        return authority;
    }

    @Transactional
    protected RoleEntity createRole(String roleName, Collection<AuthorityEntity> authorities) {

        RoleEntity role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new RoleEntity(roleName);
            role.setAuthorities(new HashSet<>(authorities));
            roleRepository.save(role);
        }

        return role;
    }
}
