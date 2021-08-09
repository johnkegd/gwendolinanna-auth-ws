package com.gwendolinanna.ws.auth.app;

import com.gwendolinanna.ws.auth.app.io.entity.AuthorityEntity;
import com.gwendolinanna.ws.auth.app.io.repositories.AuthorityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Johnkegd
 */
@Component
public class InitialUsersSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready");

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

    }

    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);

        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }

        return authority;
    }
}
