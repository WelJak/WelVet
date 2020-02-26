package com.weljak.welvet.domain.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, String> {
    List<Animal> findAllByUuid(String uuid);
}
