package com.weljak.welvet.webapi.controllers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.weljak.welvet.domain.animal.AnimalAlreadyExistsException;
import com.weljak.welvet.domain.appointment.AppointmentAlreadyCancelledException;
import com.weljak.welvet.domain.appointment.AppointmentAlreadyCompletedException;
import com.weljak.welvet.domain.appointment.AppointmentAlreadyPostponedException;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.domain.registration.RegistrationConfirmationIdDoesNotMatchException;
import com.weljak.welvet.domain.registration.RegistrationRequestAlreadyExistsException;
import com.weljak.welvet.webapi.utils.WelVetResponse;
import com.weljak.welvet.webapi.utils.WelVetResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({JsonParseException.class, JsonMappingException.class})
    public ResponseEntity<WelVetResponse> malformedRequestPayload(Exception e, WebRequest request) {
        log.warn("Malformed request payload: {}", e.getMessage());
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "MalformedPayload", "Malformed request payload", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<WelVetResponse> resourceNotFound(NullPointerException npe, WebRequest request) {
        log.warn("Resource not found: {}", request.getDescription(false));
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "ResourceNotFound", "Resource not found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppointmentAlreadyCancelledException.class, AppointmentAlreadyCompletedException.class, AppointmentAlreadyPostponedException.class})
    public ResponseEntity<WelVetResponse> appointmentStatusBlocked(Exception e, WebRequest request) {
        log.warn("Trying to change appointment status which is unchangeable");
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "ChangeStatusError", "Appointment status can not be changed", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnimalAlreadyExistsException.class)
    public ResponseEntity<WelVetResponse> animalAlreadyExists(Exception e, WebRequest request) {
        log.warn("Trying to create animal which already exists");
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "AnimalAlreadyExists", "Animal with given name and owner already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OwnerAlreadyExistsException.class)
    public ResponseEntity<WelVetResponse> ownerAlreadyExists(Exception e, WebRequest request) {
        log.warn("Trying to create new owner with already used username: {}", request.getParameter("json"));
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "UserAlreadyExists", "Account with given username already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationRequestAlreadyExistsException.class)
    public ResponseEntity<WelVetResponse> registrationRequestAlreadyExists(Exception e, WebRequest request) {
        log.warn("Trying to create new registration request when there is already one with the same username");
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "RegistrationRequestAlreadyExists", "Registration request with given username already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationConfirmationIdDoesNotMatchException.class)
    public ResponseEntity<WelVetResponse> registrationConfirmationIdMismatch(Exception e, WebRequest request) {
        log.warn("Registration confirmation id does not match with given confimration id: {}", request.getDescription(false));
        return new ResponseEntity<>(WelVetResponseUtils.errorWelVetResponse(request.getDescription(false), "RegistrationConfirmationIdMismatch", "Registration request confirmation id does not match", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

}
