package com.weljak.welvet.service.owner;

import com.weljak.welvet.domain.owner.Owner;

public interface OwnerService {
    Owner findOwnerByUuid(String uuid);

    Owner findOwnerByUsername(String username);

    Owner createOwner(String username, String password);
}
