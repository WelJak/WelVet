package com.weljak.welvet.service.owner;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.domain.owner.OwnerRepo;
import com.weljak.welvet.security.CurrentOwner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService, UserDetailsService {
    private final OwnerRepo ownerRepo;

    @Override
    public Owner createOwner(String username, String password) {
        if (ownerRepo.existsByUsername(username) == true){
            throw new OwnerAlreadyExistsException();
        }
        Owner owner = Owner.builder()
                .uuid(UUID.randomUUID().toString())
                .username(username)
                .password(password)
                .role("USER_ROLE")
                .build();
        ownerRepo.save(owner);
        return owner;
//        return ownerRepo.save(Owner.builder()
//                .uuid(UUID.randomUUID().toString())
//                .username(username)
//                .password(password)
//                .role("USER_ROLE")
//                .build());
    }

    @Override
    public Owner findByUsername(String username) {
        return ownerRepo.findByUsername(username);
    }

    @Override
    public Owner findByUuid(String uuid) {
        return ownerRepo.findByUuid(uuid);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Owner owner = ownerRepo.findByUsername(s);
       // User user = new User(owner.getUsername(), owner.getPassword(), AuthorityUtils.createAuthorityList(owner.getRole()));
        CurrentOwner currentOwner = new CurrentOwner(owner);
        return currentOwner;
    }
}
