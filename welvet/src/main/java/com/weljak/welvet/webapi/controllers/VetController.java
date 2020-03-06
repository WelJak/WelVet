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
        try {
            Owner owner = vetService.findOwnerByUsername(username);
            List<Animal> animals = vetService.findAllOwnerAnimals(owner);
            VetGetOwnerDetailsResponse ownerDetails = VetGetOwnerDetailsResponse.builder()
                    .username(username)
                    .uuid(owner.getUuid())
                    .animalsOwned(animalsToStringList(animals))
                    .build();
            return WelVetResponseUtils.success(Endpoints.VET_GET_OWNER_BY_USERNAME_ENDPOINT, ownerDetails, "Fetched owner details", HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_OWNER_BY_USERNAME_ENDPOINT, "UserNotFoundEXception", "User with given username does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_OWNER_BY_USERNAME_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnerDetailsByUserUuid(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Finding user with username: {}", uuid);
        try {
            Owner owner = vetService.findOwnerByUUID(uuid);
            List<Animal> animals = vetService.findAllOwnerAnimals(owner);
            VetGetOwnerDetailsResponse ownerDetails = VetGetOwnerDetailsResponse.builder()
                    .username(owner.getUsername())
                    .uuid(owner.getUuid())
                    .animalsOwned(animalsToStringList(animals))
                    .build();
            return WelVetResponseUtils.success(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT, ownerDetails, "Fetched owner details", HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT, "UserNotFoundEXception", "User with given uuid does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_OWNER_BY_UUID_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(Endpoints.VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> changeAnimalTreatment(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody VetChangeAnimalTreatmentRequest treatmentRequest) {
        log.info("Updating treatment prescription for animal: {}", treatmentRequest.getAnimalId());
        try {
            Animal animal = vetService.changeAnimalTreatment(treatmentRequest.getAnimalId(), treatmentRequest.getTreatment());
            VetChangeTreatmentResponse response = VetChangeTreatmentResponse.builder()
                    .animalName(animal.getName())
                    .animalId(animal.getAnimalId())
                    .owner(animal.getUuid().getUuid())
                    .newTreatment(treatmentRequest.getTreatment())
                    .build();
            return WelVetResponseUtils.success(Endpoints.VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT, response, "Treatment changed", HttpStatus.CREATED);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAnimalById(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String animalId) {
        try {
            Animal animal = vetService.findAnimalById(animalId);
            FindAnimalResponse response = FindAnimalResponse.builder()
                    .animalId(animalId)
                    .age(animal.getAge())
                    .breed(animal.getBreed())
                    .name(animal.getName())
                    .type(animal.getType())
                    .treatment(animal.getTreatment())
                    .owner(animal.getUuid().getUuid())
                    .build();
            return WelVetResponseUtils.success(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT, response, "Fetched animal details", HttpStatus.OK);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT, "AnimalNotFoundError", "Animal with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_ANIMAL_BY_ID_ENDPOINT, "UnknownError", "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> animalsToStringList(List<Animal> animals) {
        List<String> idList = new ArrayList<>();
        for (Animal animal : animals) {
            idList.add(animal.getAnimalId());
        }
        return idList;
    }
}
