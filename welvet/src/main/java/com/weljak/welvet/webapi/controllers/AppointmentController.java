package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.appointment.*;
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
        try {
            AppointmentRequest appointmentRequest = requestService.getRequestByUuid(uuid);
            AppointmentRequestDetailsResponse response = toAppointmentRequestDetailsResponse(appointmentRequest);
            return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, response, "Getting appointment request details", HttpStatus.OK);
        } catch (NullPointerException NPE) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, "RequestNotFoundException", "Request with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.GET_APPOINTMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Getting appointment details with id: {}", uuid);
        try {
            Appointment appointment = appointmentService.getAppointmentByUuid(uuid);
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
            return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_ENDPOINT, response, "Got appointment details", HttpStatus.OK);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_ENDPOINT, "AppointmentDoesNotExistsError", "Appointment with given id does not exist", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_ENDPOINT, "UnknownError", "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.VET_GET_ALL_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAllAppointmentsDetails(@AuthenticationPrincipal CurrentOwner currentOwner) {
        try {
            List<Appointment> appointmentList = appointmentService.getAllAppointments();
            List<AppointmentDetailsResponse> responseList = toAppointmentDetailsResponseList(appointmentList);
            return WelVetResponseUtils.success(Endpoints.VET_GET_ALL_APPOINTMENTS_ENDPOINT, responseList, "Fetching all appointments", HttpStatus.OK);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_GET_ALL_APPOINTMENTS_ENDPOINT, "UnknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(Endpoints.VET_DELETE_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> deleteAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        try {
            appointmentService.deleteAppointmentByUuid(uuid);
            return WelVetResponseUtils.noContent();
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_DELETE_APPOINTMENTS_ENDPOINT, "UnknownError", "Unknowne error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Endpoints.CREATE_APPOINTMENT_REQUEST_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAppointmentRequest(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody AppointmentRequestCreationForm requestForm) {
        log.info("Creating new appointment request");
        try {
            AppointmentRequest request = requestService.createAppointmentRequest(requestForm, currentOwner.getCurrentOwner());
            AppointmentRequestDetailsResponse response = toAppointmentRequestDetailsResponse(request);
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
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
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

    @PutMapping(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> cancelAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        log.info("Canceling appointment with id: {}", uuid);
        try {
            Appointment cancelledAppointment = appointmentService.changeAppointmentStatus(uuid, AppointmentStatus.CANCELED);
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(cancelledAppointment);
            return WelVetResponseUtils.success(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, response, "Status changed to CANCELED", HttpStatus.CREATED);
        } catch (AppointmentAlreadyPostponedException aape) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, "AppointmentAlreadyPostponedError", "Appointment with given id is already postponed", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCancelledException aace) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, "AppointmentAlreadyCancelledError", "Appointment with given id is already cancelled", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCompletedException aace) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, "AppointmentAlreadyCompletedError", "Appointment with given id is already completed", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, "AppointmentNotFoundError", "Appointment with given id does not exist", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.CANCEL_APPOINTMENTS_ENDPOINT, "UknownError", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getOwnersPendingRequests(@AuthenticationPrincipal CurrentOwner currentOwner) {
        log.info("Getting all pending appointment request for user: {}", currentOwner.getOwnerUUID());
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
        try {
            Appointment appointment = appointmentService.changeAppointmentStatus(uuid, AppointmentStatus.COMPLETED);
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
            return WelVetResponseUtils.success(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, response, "Appointment status changed", HttpStatus.CREATED);
        } catch (AppointmentAlreadyPostponedException aape) {
            return WelVetResponseUtils.error(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, "AppointmentAlreadyPostponedError", "Appointment with given id is already postponed", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCancelledException aace) {
            return WelVetResponseUtils.error(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, "AppointmentAlreadyCancelledError", "Appointment with given id is already cancelled", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCompletedException aace) {
            return WelVetResponseUtils.error(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, "AppointmentAlreadyCompletedError", "Appointment with given id is already completed", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, "AppointmentNotFoundError", "Appointment with given id does not exists", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT, "UnknownError", "Unknowne error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT)
    public ResponseEntity<WelVetResponse> postponeAppointment(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody PostponeAppointmentRequest postponeRequest) {
        log.info("Postponing appointment with id: {}", postponeRequest.getUuid());
        try {
            Appointment postponedAppointment = appointmentService.changeAppointmentStatus(postponeRequest.getUuid(), AppointmentStatus.POSTPONED);
            Appointment newAppointment = appointmentService.createAppointment(
                    postponedAppointment.getAnimalId(),
                    currentOwner.getCurrentOwner(),
                    postponeRequest.getDate(),
                    postponedAppointment.getType()
            );
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(newAppointment);
            return WelVetResponseUtils.success(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, response, "Appointment postponed", HttpStatus.CREATED);
        } catch (AppointmentAlreadyPostponedException aape) {
            return WelVetResponseUtils.error(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, "AppointmentAlreadyPostponedError", "Appointment with given id is already postponed", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCancelledException aace) {
            return WelVetResponseUtils.error(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, "AppointmentAlreadyCancelledError", "Appointment with given id is already cancelled", HttpStatus.BAD_REQUEST);
        } catch (AppointmentAlreadyCompletedException aace) {
            return WelVetResponseUtils.error(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, "AppointmentAlreadyCompletedError", "Appointment with given id is already completed", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException npe) {
            return WelVetResponseUtils.error(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, "AppointmentDoesNotExistError", "Appointment with given id does not exist", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return WelVetResponseUtils.error(Endpoints.VET_POSTPONE_APPOINTMENT_ENDPOINT, "UnknownError", "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Endpoints.VET_CREATE_APPOINTMENT_PROPOSITION_ENDPOINT)
    public ResponseEntity<WelVetResponse> createAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @RequestBody CreateAppointmentPropositionRequest appointmentPropositionRequest){
        log.info("Creating appointment proposition for request with id: {}", appointmentPropositionRequest.getRequestId());
        try{
            AppointmentProposition appointmentProposition = propositionService.createAppointmentProposition(
                    currentOwner.getCurrentOwner(),
                    appointmentPropositionRequest.getRequestId(),
                    appointmentPropositionRequest.getNewDate()
            );
            AppointmentPropositionDetailsResponse response = toAppointmentPropositionDetailsRepsone(appointmentProposition);
            return WelVetResponseUtils.success(Endpoints.VET_CREATE_APPOINTMENT_PROPOSITION_ENDPOINT, response, "Created appointment proposition", HttpStatus.CREATED);
        }catch (Exception e){
            return WelVetResponseUtils.error(Endpoints.VET_CREATE_APPOINTMENT_PROPOSITION_ENDPOINT, "UnknownError", "Unknwon Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentPropositionDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid){
        log.info("Getting appointment proposition detials with id: {}", uuid);
        try {
            AppointmentPropositionDetailsResponse response = toAppointmentPropositionDetailsRepsone(propositionService.getAppointmentPropositionByUuid(uuid));
            return WelVetResponseUtils.success(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT, response, "Fetched appointment proposition details", HttpStatus.OK);
        }catch (NullPointerException npe){
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT, "PropositionDoesNotExistsError", "Appointment proposition with given id does not exists", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return WelVetResponseUtils.error(Endpoints.GET_APPOINTMENT_PROPOSITION_DETAILS_ENDPOINT, "UnknownError", "Unknwon Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(Endpoints.VET_DELETE_APPOINTMENT_PROPOSITIONS_ENDPOINT)
    public ResponseEntity<WelVetResponse> deleteAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid){
        log.info("Deleting appointment proposition with id: {}", uuid);
        try {
            propositionService.deleteAppointmentProposition(uuid);
            return WelVetResponseUtils.noContent();
        }catch (NullPointerException npe){
            return WelVetResponseUtils.error(Endpoints.VET_DELETE_APPOINTMENT_PROPOSITIONS_ENDPOINT, "PropositionDoesNotExistsError", "Appointment proposition with given id does not exists", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return WelVetResponseUtils.error(Endpoints.VET_DELETE_APPOINTMENT_PROPOSITIONS_ENDPOINT, "UnknownError", "Unknwon Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT)
    public ResponseEntity<WelVetResponse> confirmAppointmentProposition(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid){
        log.info("Confirming appointment proposition with id: {}", uuid);
        try{
            Appointment appointment = propositionService.confirmAppointmentProposition(uuid);
            AppointmentDetailsResponse response = toAppointmentDetailsResponse(appointment);
            return WelVetResponseUtils.success(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT, response, "Confirmed appointment proposition", HttpStatus.CREATED);
        }catch (NullPointerException npe){
            return WelVetResponseUtils.error(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT, "PropositionDoesNotExistsError", "Appointment proposition with given id does not exists", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return WelVetResponseUtils.error(Endpoints.OWNER_CONFIRM_APPOINTMENT_PROPOSITIONS_ENDPOINT, "UnknownError", "Unknwon Error", HttpStatus.INTERNAL_SERVER_ERROR);
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

    private AppointmentPropositionDetailsResponse toAppointmentPropositionDetailsRepsone(AppointmentProposition appointmentProposition){
        return  new AppointmentPropositionDetailsResponse(
                appointmentProposition.getUuid(),
                appointmentProposition.getRequest().getUuid(),
                appointmentProposition.getVet().getUuid(),
                appointmentProposition.getNewDate()
        );
    }
}
