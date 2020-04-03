package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.vet.VetService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.VetChangeAnimalTreatmentRequest;
import com.weljak.welvet.webapi.responses.FindAnimalResponse;
import com.weljak.welvet.webapi.responses.VetChangeTreatmentResponse;
import com.weljak.welvet.webapi.responses.VetGetOwnerDetailsResponse;
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
@RequiredArgsConstructor
@RestController
public class VetController {
    private final VetService vetService;

    @GetMapping(Endpoints.VET_GET_OWNER_BY_USERNAME_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnerDetailsByUsername(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String username) {
        log.info("Finding user with username: {}", username);
        Owner owner = vetService.findOwnerByUsername(username);
        List<Animal> animals = vetService.findAllOwnerAnimals(owner);
        VetGetOwnerDetailsResponse ownerDetails = toVetGetOwnerDetailsResponse(owner, animals);
        return WelVetResponseUtils.success(Endpoints.VET_GET_OWNER_BY_USERNAME_ENDPOINT, ownerDetails, "Fetched owner details", HttpStatus.OK);

    }

    @GetMapping(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnerDetailsByUserUuid(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Finding user with username: {}", uuid);

        Owner owner = vetService.findOwnerByUUID(uuid);
        List<Animal> animals = vetService.findAllOwnerAnimals(owner);
        VetGetOwnerDetailsResponse ownerDetails = toVetGetOwnerDetailsResponse(owner, animals);
        return WelVetResponseUtils.success(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT, ownerDetails, "Fetched owner details", HttpStatus.OK);

    }

    @PutMapping(Endpoints.VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> changeAnimalTreatment(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody VetChangeAnimalTreatmentRequest treatmentRequest) {
        log.info("Updating treatment prescription for animal: {}", treatmentRequest.getAnimalId());
        Animal animal = vetService.changeAnimalTreatment(treatmentRequest.getAnimalId(), treatmentRequest.getTreatment());
        VetChangeTreatmentResponse response = toVetChangeTreatmentResponse(animal, treatmentRequest);
        return WelVetResponseUtils.success(Endpoints.VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT, response, "Treatment changed", HttpStatus.CREATED);

    }

    @GetMapping(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAnimalById(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String animalId) {
        Animal animal = vetService.findAnimalById(animalId);
        FindAnimalResponse response = toFindAnimalResponse(animal);
        return WelVetResponseUtils.success(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT, response, "Fetched animal details", HttpStatus.OK);

    }

    private VetChangeTreatmentResponse toVetChangeTreatmentResponse(Animal animal, VetChangeAnimalTreatmentRequest treatmentRequest) {
        return VetChangeTreatmentResponse.builder()
                .animalName(animal.getName())
                .animalId(animal.getAnimalId())
                .owner(animal.getUuid().getUuid())
                .newTreatment(treatmentRequest.getTreatment())
                .build();
    }

    private FindAnimalResponse toFindAnimalResponse(Animal animal) {
        return FindAnimalResponse.builder()
                .age(animal.getAge())
                .name(animal.getName())
                .type(animal.getType())
                .animalId(animal.getAnimalId())
                .breed(animal.getBreed())
                .treatment(animal.getTreatment())
                .owner(animal.getUuid().getUuid())
                .build();
    }

    private VetGetOwnerDetailsResponse toVetGetOwnerDetailsResponse(Owner owner, List<Animal> animals) {
        return VetGetOwnerDetailsResponse.builder()
                .username(owner.getUsername())
                .uuid(owner.getUuid())
                .animalsOwned(animalsToStringList(animals))
                .build();
    }

    private List<String> animalsToStringList(List<Animal> animals) {
        List<String> idList = new ArrayList<>();
        for (Animal animal : animals) {
            idList.add(animal.getAnimalId());
        }
        return idList;
    }
}
