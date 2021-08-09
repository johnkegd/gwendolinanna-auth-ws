package com.gwendolinanna.ws.auth.app;

import com.gwendolinanna.ws.auth.app.io.entity.AuthorityEntity;
import com.gwendolinanna.ws.auth.app.io.entity.RoleEntity;
import com.gwendolinanna.ws.auth.app.io.entity.UserEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.AuthorityRepository;
import com.gwendolinanna.ws.auth.app.io.repositories.RoleRepository;
import com.gwendolinanna.ws.auth.app.io.repositories.UserRepository;
import com.gwendolinanna.ws.auth.app.shared.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        RoleEntity roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if (roleAdmin == null) return;

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("John");
        adminUser.setLastName("Garcia");
        adminUser.setEmail("john@gwendolinanna.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(10));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("hola"));
        adminUser.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
        

        userRepository.save(adminUser);

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
