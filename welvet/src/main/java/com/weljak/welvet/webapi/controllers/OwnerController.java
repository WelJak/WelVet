package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.security.CurrentOwner;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(Endpoints.CREATE_OWNER_ENDPOINT)
    public ResponseEntity<WelVetResponse> createOwner(@RequestBody CreateOwnerRequest ownerRequest) {
        try {
            log.info("Creating new user with request username:{}, password: {}", ownerRequest.getUsername(), ownerRequest.getPassword());
            Owner newOwner = ownerService.createOwner(ownerRequest.getUsername(), bCryptPasswordEncoder.encode(ownerRequest.getPassword()));
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.success(Endpoints.CREATE_OWNER_ENDPOINT, newOwner, "Owner created", HttpStatus.CREATED);
            return response;
        } catch (OwnerAlreadyExistsException OAEE) {
            log.info("Create user request rejected. Reason: Username is not available");
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "OwnerAlreadyExistsException", "User with this username already exists", HttpStatus.BAD_REQUEST);
            return response;
        } catch (Exception e) {
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "UnknownException", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

    }

    @GetMapping(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getCurrentOwnerDetails(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            log.info("Getting account details for user with uuid: {}", currentOwner.getOwnerUUID());
            Owner owner = ownerService.findByUuid(currentOwner.getOwnerUUID());
            FindOwnerResponse findOwnerResponse = new FindOwnerResponse(owner.getUuid(), owner.getUsername(), owner.getRole());
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.success(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT, findOwnerResponse, String.format("found owner with uuid: %s", currentOwner.getOwnerUUID()), HttpStatus.OK);
            return response;
        } catch (NullPointerException NPE) {
            ResponseEntity<WelVetResponse> response = WelVetResponseUtils.error(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT, "UserDoesNotExists", "User with this username does not exists", HttpStatus.BAD_REQUEST);
            return response;
        }
    }

//    //ADD AFTER IMPLEMENTING ANIMAL SERVICE
//    @GetMapping
//    public ResponseEntity<WelVetResponse> findAllOwnersAnimals(){
//
//    }

}
