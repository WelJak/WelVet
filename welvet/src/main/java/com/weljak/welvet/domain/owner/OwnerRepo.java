package com.weljak.welvet.domain.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepo extends JpaRepository<Owner, String> {
    Owner findByUuid(String uuid);

    boolean existsByUsername(String username);

    Owner findByUsername(String username);
}
