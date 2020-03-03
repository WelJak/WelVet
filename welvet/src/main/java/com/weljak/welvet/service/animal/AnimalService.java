package com.weljak.welvet.service.animal;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.webapi.requests.CreateAnimalRequest;

import java.util.List;

public interface AnimalService {
    Animal findAnimalById(String id);

    Animal createAnimal(CreateAnimalRequest createAnimalRequest, Owner owner);

    List<Animal> findAllAnimalsByOwnerUUID(Owner uuid);
}
