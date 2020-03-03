package com.weljak.welvet.service.animal;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.animal.AnimalAlreadyExistsException;
import com.weljak.welvet.domain.animal.AnimalRepo;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.webapi.requests.CreateAnimalRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepo animalRepo;

    @Override
    public Animal createAnimal(CreateAnimalRequest createAnimalRequest, Owner owner) {
        if (animalRepo.existsByUuidAndName(owner, createAnimalRequest.getName())) {
            throw new AnimalAlreadyExistsException();
        }
        Animal newAnimal = Animal.builder()
                .animalId(UUID.randomUUID().toString())
                .uuid(owner)
                .name(createAnimalRequest.getName())
                .type(createAnimalRequest.getType())
                .breed(createAnimalRequest.getBreed())
                .age(createAnimalRequest.getAge())
                .treatment("No treatment.")
                .build();
        animalRepo.save(newAnimal);
        return newAnimal;
    }

    @Override
    public Animal findAnimalById(String id) {
        return animalRepo.findByAnimalId(id);
    }


    @Override
    public List<Animal> findAllAnimalsByOwnerUUID(Owner uuid) {
        return animalRepo.findAllByUuid(uuid);
    }
}
