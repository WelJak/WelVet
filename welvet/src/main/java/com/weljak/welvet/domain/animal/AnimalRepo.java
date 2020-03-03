package com.weljak.welvet.domain.animal;

import com.weljak.welvet.domain.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, String> {
    Animal findByAnimalId(String animalId);

    List<Animal> findAllByUuid(Owner uuid);

    Boolean existsByUuidAndName(Owner owner, String name);
}
