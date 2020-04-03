package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointment.AppointmentStatus;
import com.weljak.welvet.domain.appointmentproposition.AppointmentProposition;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.appointments.appointment.AppointmentService;
import com.weljak.welvet.service.appointments.appointmentproposition.AppointmentPropositionService;
import com.weljak.welvet.service.appointments.appointmentrequest.AppointmentRequestService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.requests.AppointmentRequestCreationForm;
import com.weljak.welvet.webapi.requests.CreateAppointmentPropositionRequest;
import com.weljak.welvet.webapi.requests.PostponeAppointmentRequest;
import com.weljak.welvet.webapi.responses.AppointmentDetailsResponse;
import com.weljak.welvet.webapi.responses.AppointmentPropositionDetailsResponse;
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
    private final AppointmentPropositionService propositionService;

    @GetMapping(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentRequestDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Getting appointment request details for request: {}", uuid);
        AppointmentRequest appointmentRequest = requestService.getRequestByUuid(uuid);
        AppointmentRequestDetailsResponse response = toAppointmentRequestDetailsResponse(appointmentRequest);
        return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, response, "Getting appointment request details", HttpStatus.OK);

    }

    @GetMapping(Endpoints.GET_APPOINTMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Getting appointment details with id: {}", uuid);
        Appointment appointment = appointmentService.getAppointmentByUuid(uuid);
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
        return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_ENDPOINT, response, "Got appointment details", HttpStatus.OK);
    }

    @GetMapping(Endpoints.VET_GET_ALL_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAllAppointmentsDetails(@AuthenticationPrincipal CurrentOwner currentOwner) {
        log.info("Getting details of all appointments for user: {}", currentOwner.getOwnerUUID());
        List<Appointment> appointmentList = appointmentService.getAllAppointments();
        List<AppointmentDetailsResponse> responseList = toAppointmentDetailsResponseList(appointmentList);
        return WelVetResponseUtils.success(Endpoints.VET_GET_ALL_APPOINTMENTS_ENDPOINT, responseList, "Fetching all appointments", HttpStatus.OK);

    }

    @DeleteMapping(Endpoints.VET_DELETE_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> deleteAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Deleting appointment with id: {}", uuid);
        appointmentService.deleteAppointmentByUuid(uuid);
        return WelVetResponseUtils.noContent();

    }

    @PostMapping(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody AppointmentRequestCreationForm requestForm) {
        log.info("Creating new appointment request");

        AppointmentRequest request = requestService.createAppointmentRequest(requestForm, currentOwner.getCurrentOwner());
        AppointmentRequestDetailsResponse response = toAppointmentRequestDetailsResponse(request);
        return WelVetResponseUtils.success(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT, response, "Creating appontment request", HttpStatus.CREATED);

    }

    @PostMapping(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Confirming appointment request with uuid: {}", uuid);

        Appointment appointment = requestService.confirmAppointmentRequest(currentOwner.getCurrentOwner(), uuid);
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
        return WelVetResponseUtils.success(Endpoints.CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT, response, "Request confirmed", HttpStatus.CREATED);

    }

    @DeleteMapping(Endpoints.CANCEL_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> cancelAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Canceling appointment request with id: {}", uuid);

        requestService.deleteRequestByUuid(uuid);
        return WelVetResponseUtils.noContent();

    }

    @PutMapping(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> cancelAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Canceling appointment with id: {}", uuid);
        Appointment cancelledAppointment = appointmentService.changeAppointmentStatus(uuid, AppointmentStatus.CANCELED);
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(cancelledAppointment);
        return WelVetResponseUtils.success(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, response, "Status changed to CANCELED", HttpStatus.CREATED);

    }

    @GetMapping(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnersPendingRequests(@AuthenticationPrincipal CurrentOwner currentOwner) {
        log.info("Getting all pending appointment request for user: {}", currentOwner.getOwnerUUID());

        List<AppointmentRequest> requests = requestService.getOwnersRequests(currentOwner.getCurrentOwner());
        List<AppointmentRequestDetailsResponse> response = appointmentRequestToRequestResponseList(requests);
        return WelVetResponseUtils.success(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, response, "Got all users pending appointment requests", HttpStatus.OK);

    }

    @GetMapping(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAllPendingRequests(@AuthenticationPrincipal CurrentOwner currentOwner) {
        log.info("Getting all pending appointment requests");
        try {
            List<AppointmentRequest> requests = requestService.getAllRequests();
            List<AppointmentRequestDetailsResponse> response = appointmentRequestToRequestResponseList(requests);
            return WelVetResponseUtils.success(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, response, "Got all pending appointment request", HttpStatus.OK);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT)
    public ResponseEntity<WelVetResponse> markAppointmentCompleted(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Changing appointment status for appointment: {}, status: {}", uuid, AppointmentStatus.COMPLETED);
        Appointment appointment = appointmentService.changeAppointmentStatus(uuid, AppointmentStatus.COMPLETED);
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
        return WelVetResponseUtils.success(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, response, "Appointment status changed", HttpStatus.CREATED);

    }

    @PostMapping(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> postponeAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody PostponeAppointmentRequest postponeRequest) {
        log.info("Postponing appointment with id: {}", postponeRequest.getUuid());

        Appointment postponedAppointment = appointmentService.changeAppointmentStatus(postponeRequest.getUuid(), AppointmentStatus.POSTPONED);
        Appointment newAppointment = appointmentService.createAppointment(
                postponedAppointment.getAnimalId(),
                currentOwner.getCurrentOwner(),
                postponeRequest.getDate(),
                postponedAppointment.getType()
        );
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(newAppointment);
        return WelVetResponseUtils.success(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, response, "Appointment postponed", HttpStatus.CREATED);

    }

    @PostMapping(Endpoints.VET_CREATE_APPOINTMENT_PROPOSITION_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody CreateAppointmentPropositionRequest appointmentPropositionRequest) {
        log.info("Creating appointment proposition for request with id: {}", appointmentPropositionRequest.getRequestId());
        AppointmentProposition appointmentProposition = propositionService.createAppointmentProposition(
                currentOwner.getCurrentOwner(),
                appointmentPropositionRequest.getRequestId(),
                appointmentPropositionRequest.getNewDate()
        );
        AppointmentPropositionDetailsResponse response = toAppointmentPropositionDetailsRepsone(appointmentProposition);
        return WelVetResponseUtils.success(Endpoints.VET_CREATE_APPOINTMENT_PROPOSITION_ENDPOINT, response, "Created appointment proposition", HttpStatus.CREATED);

    }

    @GetMapping(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentPropositionDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Getting appointment proposition detials with id: {}", uuid);
        AppointmentPropositionDetailsResponse response = toAppointmentPropositionDetailsRepsone(propositionService.getAppointmentPropositionByUuid(uuid));
        return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT, response, "Fetched appointment proposition details", HttpStatus.OK);

    }

    @DeleteMapping(Endpoints.VET_DELETE_APPOINTMENT_PROPOSITIONS_ENDPOINT)
    public ResponseEntity<WelVetResponse> deleteAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Deleting appointment proposition with id: {}", uuid);
        propositionService.deleteAppointmentProposition(uuid);
        return WelVetResponseUtils.noContent();

    }

    @GetMapping(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Confirming appointment proposition with id: {}", uuid);

        Appointment appointment = propositionService.confirmAppointmentProposition(uuid);
        AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
        return WelVetResponseUtils.success(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT, response, "Confirmed appointment proposition", HttpStatus.CREATED);

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

    private List<AppointmentDetailsResponse> toAppointmentDetailsResponseList(List<Appointment> appointmentList) {
        List<AppointmentDetailsResponse> detailsResponses = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            detailsResponses.add(new AppointmentDetailsResponse(
                    appointment.getUuid(),
                    appointment.getAnimalId().getAnimalId(),
                    appointment.getVetId().getUuid(),
                    appointment.getDate(),
                    appointment.getStatus(),
                    appointment.getType()
            ));
        }
        return detailsResponses;
    }

    private AppointmentDetailsResponse toAppointmentDetailsResponse(Appointment appointment) {
        return new AppointmentDetailsResponse(
                appointment.getUuid(),
                appointment.getAnimalId().getAnimalId(),
                appointment.getVetId().getUuid(),
                appointment.getDate(),
                appointment.getStatus(),
                appointment.getType()
        );
    }

    private AppointmentRequestDetailsResponse toAppointmentRequestDetailsResponse(AppointmentRequest appointmentRequest) {
        return new AppointmentRequestDetailsResponse(
                appointmentRequest.getUuid(),
                appointmentRequest.getOwnerId().getUuid(),
                appointmentRequest.getAnimalId().getAnimalId(),
                appointmentRequest.getType(),
                appointmentRequest.getPreferredDate()
        );
    }

    private AppointmentPropositionDetailsResponse toAppointmentPropositionDetailsRepsone(AppointmentProposition appointmentProposition) {
        return new AppointmentPropositionDetailsResponse(
                appointmentProposition.getUuid(),
                appointmentProposition.getRequest().getUuid(),
                appointmentProposition.getVet().getUuid(),
                appointmentProposition.getNewDate()
        );
    }
}
