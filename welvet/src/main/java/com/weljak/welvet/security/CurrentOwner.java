package com.weljak.welvet.security;

import com.weljak.welvet.domain.owner.Owner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentOwner extends User {
    private final Owner owner;

    public CurrentOwner(Owner owner) {
        super(owner.getUsername(), owner.getPassword(), AuthorityUtils.createAuthorityList(owner.getRole()));
        this.owner = owner;
    }

    public Owner getCurrentOwner() {
        return owner;
    }

    public String getOwnerUUID() {
        return owner.getUuid();
    }

    public String getOwnerRole() {
        return owner.getRole();
    }

    public String getOwnerUsername() {
        return owner.getUsername();
    }

}
