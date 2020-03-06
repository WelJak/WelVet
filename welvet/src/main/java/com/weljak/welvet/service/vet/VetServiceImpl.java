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
public class VetServiceImpl implements VetService {
    private final OwnerRepo ownerRepo;
    private final AnimalRepo animalRepo;

    @Override
    public Owner findOwnerByUsername(String username) {
        return ownerRepo.findByUsername(username);
    }

    @Override
    public Owner findOwnerByUUID(String uuid) {
        return ownerRepo.findByUuid(uuid);
    }

    @Override
    public List<Animal> findAllOwnerAnimals(Owner uuid) {
        return animalRepo.findAllByUuid(uuid);
    }

    @Override
    public Animal findAnimalById(String animalId) {
        return animalRepo.findByAnimalId(animalId);
    }

    @Override
    public Animal changeAnimalTreatment(String animalId, String treatment) {
        Animal animal = animalRepo.findByAnimalId(animalId);
        animal.setTreatment(treatment);
        animalRepo.save(animal);
        return animal;
    }
}
