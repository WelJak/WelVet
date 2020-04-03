package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.registration.RegistrationRequest;
import com.weljak.welvet.service.registration.RegistrationService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.CreateRegistrationRequestResponse;
import com.weljak.welvet.webapi.responses.RegistrationConfirmation;
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
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> createRegistrationRequest(@RequestBody CreateOwnerRequest createOwnerRequest) {
        log.info("Creating registration request for email: {}", createOwnerRequest.getUsername());
        RegistrationRequest registrationRequest = registrationService.createRegistrationRequest(createOwnerRequest);
        CreateRegistrationRequestResponse response = new CreateRegistrationRequestResponse(registrationRequest.getUsername());
        return WelVetResponseUtils.success(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT, response, "Created registration request", HttpStatus.CREATED);

    }

    @GetMapping(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmRegistrationRequest(@PathVariable String registrationRequestId, @PathVariable String confirmationId) {
        log.info("Confirming registration with id: {} and confirmation id: {}", registrationRequestId, confirmationId);
        RegistrationConfirmation response = registrationService.confirmRegistrationRequest(registrationRequestId, confirmationId);
        return WelVetResponseUtils.success(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT, response, "Registration request confirmed", HttpStatus.CREATED);
    }
}
