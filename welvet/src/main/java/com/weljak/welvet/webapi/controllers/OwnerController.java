package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.service.owner.OwnerService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.FindOwnerResponse;
import com.weljak.welvet.webapi.utils.WelVetResponse;
import com.weljak.welvet.webapi.utils.WelVetResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping(Endpoints.CREATE_OWNER_ENDPOINT)
    public ResponseEntity<WelVetResponse> createOwner(@RequestBody CreateOwnerRequest ownerRequest) {
        try {
            log.info("Creating new user with request username:{}, password: {}", ownerRequest.getUsername(), ownerRequest.getPassword());
            Owner newOwner = ownerService.createOwner(ownerRequest.getUsername(), ownerRequest.getPassword());
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.success(Endpoints.CREATE_OWNER_ENDPOINT, newOwner, "Owner created", HttpStatus.CREATED);
            return response;
        } catch (OwnerAlreadyExistsException OAEE) {
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "OwnerAlreadyExistsException", "User with this username already exists", HttpStatus.BAD_REQUEST);
            return response;
        } catch (Exception e) {
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "UnknownException", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

    }

    //REFACTOR AFTER ADDING SPRING SECURITY
    @GetMapping(Endpoints.OWNER_ENDPOINT + "/{uuid}")
    public ResponseEntity<WelVetResponse> findOwnerByUUID(@PathVariable String uuid) {
        try {
            Owner owner = ownerService.findByUuid(uuid);
            FindOwnerResponse findOwnerResponse = new FindOwnerResponse(owner.getUuid(), owner.getUsername(), owner.getRole());
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.success("/user/{uuid}", findOwnerResponse, String.format("found owner with uuid: %s", uuid), HttpStatus.OK);
            return response;
        } catch (NullPointerException NPE) {
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error("/user/{uuid}", "UserDoesNotExists", "User with this username does not exists", HttpStatus.BAD_REQUEST);
            return response;
        }
    }

//    //ADD AFTER IMPLEMENTING ANIMAL SERVICE
//    @GetMapping
//    public ResponseEntity<WelVetResponse> findAllOwnersAnimals(){
//
//    }

}
