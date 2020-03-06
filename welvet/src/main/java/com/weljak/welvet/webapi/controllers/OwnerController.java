package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.owner.OwnerService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.CurrentOwnerDetailsResponse;
import com.weljak.welvet.webapi.utils.WelVetResponse;
import com.weljak.welvet.webapi.utils.WelVetResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            return WelVetResponseUtils.success(Endpoints.CREATE_OWNER_ENDPOINT, newOwner, "Owner created", HttpStatus.CREATED);
        } catch (OwnerAlreadyExistsException OAEE) {
            log.info("Create user request rejected. Reason: Username is not available");
            return WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "OwnerAlreadyExistsException", "User with this username already exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CREATE_OWNER_ENDPOINT, "UnknownException", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getCurrentOwnerDetails(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            log.info("Getting account details for user with uuid: {}", currentOwner.getOwnerUUID());
            Owner owner = ownerService.findOwnerByUuid(currentOwner.getOwnerUUID());
            CurrentOwnerDetailsResponse currentOwnerDetailsResponse = new CurrentOwnerDetailsResponse(owner.getUuid(), owner.getUsername(), owner.getRole());
            return WelVetResponseUtils.success(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT, currentOwnerDetailsResponse, String.format("found owner with uuid: %s", currentOwner.getOwnerUUID()), HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.CURRENT_OWNER_DETAILS_ENDPOINT, "UserDoesNotExists", "User with this username does not exists", HttpStatus.BAD_REQUEST);
        }
    }


}
