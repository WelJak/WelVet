package com.weljak.welvet.service.owner;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.domain.owner.OwnerRepo;
import com.weljak.welvet.security.CurrentOwner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresOwnerService implements OwnerService, UserDetailsService {
    private final OwnerRepo ownerRepo;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Owner createOwner(String username, String password) {
        log.info("creating owner with request username: {} and password: {}", username, password);
        if (ownerRepo.existsByUsername(username)) {
            throw new OwnerAlreadyExistsException();
        }
        Owner owner = Owner.builder()
                .uuid(UUID.randomUUID().toString())
                .username(username)
                .password(password)
                .role("ROLE_USER")
                .build();
        ownerRepo.save(owner);
        return owner;
    }

    @Override
    public Owner findOwnerByUsername(String username) {
        log.info("Fetching owner details with username: {}", username);
        return ownerRepo.findByUsername(username);
    }

    @Override
    public Owner findOwnerByUuid(String uuid) {
        log.info("Fetching owner details with uuid: {}", uuid);
        return ownerRepo.findByUuid(uuid);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Owner owner = ownerRepo.findByUsername(s);
        CurrentOwner currentOwner = new CurrentOwner(owner);
        return currentOwner;
    }
}
