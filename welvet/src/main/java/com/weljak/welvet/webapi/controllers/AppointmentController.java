package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.appointments.appointment.AppointmentService;
import com.weljak.welvet.service.appointments.appointmentrequest.AppointmentRequestService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.AppointmentRequestCreationForm;
import com.weljak.welvet.webapi.responses.AppointmentDetailsResponse;
import com.weljak.welvet.webapi.responses.AppointmentRequestDetailsResponse;
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
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentRequestService requestService;

    @GetMapping(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentRequestDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Getting appointment request details for request: {}", uuid);
        try {
            AppointmentRequest appointmentRequest = requestService.getRequestByUuid(uuid);
            AppointmentRequestDetailsResponse response = new AppointmentRequestDetailsResponse(
                    appointmentRequest.getUuid(),
                    appointmentRequest.getAnimalId().getAnimalId(),
                    appointmentRequest.getOwnerId().getUuid(),
                    appointmentRequest.getType(),
                    appointmentRequest.getPreferredDate()
            );
            return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, response, "Getting appointment request details", HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, "RequestNotFoundException", "Request with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody AppointmentRequestCreationForm requestForm) {
        log.info("Creating new appointment request");
        try {
            AppointmentRequest request = requestService.createAppointmentRequest(requestForm, currentOwner.getCurrentOwner());
            AppointmentRequestDetailsResponse response = new AppointmentRequestDetailsResponse(
                    request.getUuid(),
                    request.getOwnerId().getUuid(),
                    request.getAnimalId().getAnimalId(),
                    request.getType(),
                    request.getPreferredDate()
            );
            return WelVetResponseUtils.success(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT, response, "Creating appontment request", HttpStatus.CREATED);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Confirming appointment request with uuid: {}", uuid);
        try {
            Appointment appointment = requestService.confirmAppointmentRequest(currentOwner.getCurrentOwner(), uuid);
            AppointmentDetailsResponse response = new AppointmentDetailsResponse(
                    appointment.getUuid(),
                    appointment.getAnimalId().getAnimalId(),
                    appointment.getVetId().getUuid(),
                    appointment.getDate(),
                    appointment.getStatus(),
                    appointment.getType()
            );
            return WelVetResponseUtils.success(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT, response, "Request confirmed", HttpStatus.CREATED);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT, "RequestDoesNotExistsError", "Request with given id does not exist", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(Endpoints.CANCEL_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> cancelAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Canceling appointment request with id: {}", uuid);
        try {
            requestService.deleteRequestByUuid(uuid);
            return WelVetResponseUtils.noContent();
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENT_REQUESTS_ENDPOINT, "RequestDoesNotExistsError", "Request with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENT_REQUESTS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnersPendingRequests(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            List<AppointmentRequest> requests = requestService.getOwnersRequests(currentOwner.getCurrentOwner());
            List<AppointmentRequestDetailsResponse> response = appointmentRequestToRequestResponseList(requests);
            return WelVetResponseUtils.success(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, response, "Got all users pending appointment requests", HttpStatus.OK);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, "UnknownError", "Unknowne error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAllPendingRequests(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            List<AppointmentRequest> requests = requestService.getAllRequests();
            List<AppointmentRequestDetailsResponse> response = appointmentRequestToRequestResponseList(requests);
            return WelVetResponseUtils.success(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, response, "Got all pending appointment request", HttpStatus.OK);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<AppointmentRequestDetailsResponse> appointmentRequestToRequestResponseList(List<AppointmentRequest> appointmentRequests) {
        List<AppointmentRequestDetailsResponse> detailsResponses = new ArrayList<>();
        for (AppointmentRequest request : appointmentRequests) {
            detailsResponses.add(new AppointmentRequestDetailsResponse(
                    request.getUuid(),
                    request.getOwnerId().getUuid(),
                    request.getAnimalId().getAnimalId(),
                    request.getType(),
                    request.getPreferredDate()
            ));
        }
        return detailsResponses;
    }
}
