package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.animal.AnimalAlreadyExistsException;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.animal.AnimalService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.CreateAnimalRequest;
import com.weljak.welvet.webapi.responses.CreateAnimalResponse;
import com.weljak.welvet.webapi.responses.FindAnimalResponse;
import com.weljak.welvet.webapi.utils.WelVetResponse;
import com.weljak.welvet.webapi.utils.WelVetResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalService animalService;

    @PostMapping(Endpoints.CREATE_ANIMAL_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAnimal(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody CreateAnimalRequest createAnimalRequest) {
        try {
            Animal newAnimal = animalService.createAnimal(createAnimalRequest, currentOwner.getCurrentOwner());
            CreateAnimalResponse animalResponse = new CreateAnimalResponse(
                    newAnimal.getAnimalId(),
                    newAnimal.getUuid().getUuid(),
                    newAnimal.getName(),
                    newAnimal.getType(),
                    newAnimal.getBreed(),
                    newAnimal.getAge()
            );
            return WelVetResponseUtils.success(Endpoints.CREATE_ANIMAL_ENDPOINT, animalResponse, "animal created", HttpStatus.CREATED);
        } catch (AnimalAlreadyExistsException AAEE) {
            return WelVetResponseUtils.error(Endpoints.CREATE_ANIMAL_ENDPOINT, "AnimalAlreadyExistsException", "Animal with given name and owner currently exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CREATE_ANIMAL_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.GET_CURRENT_OWNER_ALL_ANIMALS)
    public ResponseEntity<WelVetResponse> fetchOwnerAnimals(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            List<Animal> animals = animalService.findAllAnimalsByOwnerUUID(currentOwner.getCurrentOwner());
            List<FindAnimalResponse> animalsResponseList = new ArrayList<>();
            for (Animal animal : animals) {
                animalsResponseList.add(FindAnimalResponse.builder()
                        .age(animal.getAge())
                        .name(animal.getName())
                        .type(animal.getType())
                        .animalId(animal.getAnimalId())
                        .breed(animal.getBreed())
                        .treatment(animal.getTreatment())
                        .owner(currentOwner.getOwnerUUID())
                        .build());
            }
            return WelVetResponseUtils.success(Endpoints.GET_CURRENT_OWNER_ALL_ANIMALS, animalsResponseList, String.format("Fetched all owned animals by owner with uuid: %s", currentOwner.getOwnerUUID()), HttpStatus.OK);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.GET_CURRENT_OWNER_ALL_ANIMALS, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.GET_ANIMAL_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAnimal(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String id) {
        try {
            log.info("Fetching animal details for animal with owner UUID: {} and animal ID: {}", currentOwner.getOwnerUUID(), id);
            Animal animal = animalService.findAnimalById(id);
            if (!animal.getUuid().getUuid().equals(currentOwner.getOwnerUUID())) {
                return WelVetResponseUtils.error(Endpoints.GET_ANIMAL_ENDPOINT, "AnimalNotOwnedException", "Animal with given is not owned by user", HttpStatus.FORBIDDEN);
            }
            FindAnimalResponse animalResponse = FindAnimalResponse.builder()
                    .age(animal.getAge())
                    .name(animal.getName())
                    .type(animal.getType())
                    .animalId(animal.getAnimalId())
                    .breed(animal.getBreed())
                    .treatment(animal.getTreatment())
                    .owner(currentOwner.getOwnerUUID())
                    .build();
            return WelVetResponseUtils.success(Endpoints.GET_ANIMAL_ENDPOINT, animalResponse, "Fetching animal details", HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.GET_ANIMAL_ENDPOINT, "AnimalNotFoundException", "Animal with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.GET_ANIMAL_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
