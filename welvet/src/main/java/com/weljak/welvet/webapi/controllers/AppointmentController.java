package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.security.CurrentOwner;
import com.weljak.welvet.service.appointments.appointment.AppointmentService;
import com.weljak.welvet.service.appointments.appointmentrequest.AppointmentRequestService;
import com.weljak.welvet.webapi.Endpoints;
import com.weljak.welvet.webapi.responses.AppointmentRequestDetailsResponse;
import com.weljak.welvet.webapi.utils.WelVetResponse;
import com.weljak.welvet.webapi.utils.WelVetResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentRequestService requestService;

    @GetMapping(Endpoints.GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT)
    public ResponseEntity<WelVetResponse> getAppointmentRequestDetails(@AuthenticationPrincipal CurrentOwner currentOwner, @PathVariable String uuid) {
        try {
            AppointmentRequest appointmentRequest = requestService.getRequestByUuid(uuid);
            AppointmentRequestDetailsResponse response = new AppointmentRequestDetailsResponse(
                    appointmentRequest.getUuid(),
                    appointmentRequest.getAnimalId().getAnimalId(),
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
}
