package com.weljak.welvet.service.owner;

import com.weljak.welvet.domain.owner.Owner;

public interface OwnerService {
    Owner findByUuid(String uuid);
    Owner findByUsername(String username);
    Owner createOwner(String username, String password);
}
