package com.weljak.welvet.domain.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRequestRepo extends JpaRepository<RegistrationRequest, String> {
    RegistrationRequest getByUuid(String uuid);

    RegistrationRequest getByUsername(String username);

    boolean existsByUsername(String username);
}
