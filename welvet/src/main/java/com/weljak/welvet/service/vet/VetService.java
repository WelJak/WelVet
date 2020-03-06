package com.weljak.welvet.service.vet;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.owner.Owner;

import java.util.List;

public interface VetService {
    Owner findOwnerByUsername(String username);
    Owner findOwnerByUUID(String uuid);
    Animal findAnimalById(String animalId);
    List<Animal> findAllOwnerAnimals(Owner uuid);
    Animal changeAnimalTreatment(String animalId, String treatment);
}
