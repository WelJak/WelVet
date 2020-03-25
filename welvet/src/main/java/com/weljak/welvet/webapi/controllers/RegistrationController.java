package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.domain.registration.RegistrationConfirmationIdDoesNotMatchException;
import com.weljak.welvet.domain.registration.RegistrationRequest;
import com.weljak.welvet.domain.registration.RegistrationRequestAlreadyExistsException;
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
        try {
            RegistrationRequest registrationRequest = registrationService.createRegistrationRequest(createOwnerRequest);
            CreateRegistrationRequestResponse response = new CreateRegistrationRequestResponse(registrationRequest.getUsername());
            return WelVetResponseUtils.success(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT, response, "Created registration request", HttpStatus.CREATED);
        } catch (RegistrationRequestAlreadyExistsException rraee) {
            return WelVetResponseUtils.error(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT, "RegistrationRequestAlreadyExistsError", "Registration request for given email already exist. Please check your mail box", HttpStatus.BAD_REQUEST);
        } catch (OwnerAlreadyExistsException oaee) {
            return WelVetResponseUtils.error(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT, "OwnerAlreadyExistsError", "User with given e-mail already exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT, "UnknownError", "Unknwon error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmRegistrationRequest(@PathVariable String registrationRequestId, @PathVariable String confirmationId) {
        log.info("Confirming registration with id: {} and confirmation id: {}", registrationRequestId, confirmationId);
        try {
            RegistrationConfirmation response = registrationService.confirmRegistrationRequest(registrationRequestId, confirmationId);
            return WelVetResponseUtils.success(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT, registrationRequestId, "Registration request confirmed", HttpStatus.CREATED);
        } catch (RegistrationConfirmationIdDoesNotMatchException rcidnme) {
            return WelVetResponseUtils.error(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT, "RegistrationConfirmationIdDoesNotMatchError", "Inccorect confirmation id", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT, "UnknownError", "Unknwon error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
