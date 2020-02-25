package com.weljak.welvet.domain.vet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VetRepo extends JpaRepository<Vet, String> {
    Vet findByUuid(String uuid);
    List<Vet> findAllBySpecialization(String specialization);
}
