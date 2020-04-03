package com.weljak.welvet.service.vet;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.animal.AnimalRepo;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresVetService implements VetService {
    private final OwnerRepo ownerRepo;
    private final AnimalRepo animalRepo;

    @Override
    public Owner findOwnerByUsername(String username) {
        log.info("Getting details of owner with username: {}", username);
        return ownerRepo.findByUsername(username);
    }

    @Override
    public Owner findOwnerByUUID(String uuid) {
        log.info("Getting details of owner with uuid: {}", uuid);
        return ownerRepo.findByUuid(uuid);
    }

    @Override
    public List<Animal> findAllOwnerAnimals(Owner uuid) {
        log.info("Getting list of animals owned by: {}", uuid.getUuid());
        return animalRepo.findAllByUuid(uuid);
    }

    @Override
    public Animal findAnimalById(String animalId) {
        log.info("Getting details of animal with id: {}", animalId);
        return animalRepo.findByAnimalId(animalId);
    }

    @Override
    public Animal changeAnimalTreatment(String animalId, String treatment) {
        log.info("Changing treatment for animal: {}", animalId);
        Animal animal = animalRepo.findByAnimalId(animalId);
        animal.setTreatment(treatment);
        animalRepo.save(animal);
        return animal;
    }
}
