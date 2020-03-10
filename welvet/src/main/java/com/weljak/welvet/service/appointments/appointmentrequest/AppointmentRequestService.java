package com.weljak.welvet.service.appointments.appointmentrequest;

import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.webapi.requests.AppointmentRequestCreationForm;

import java.util.List;

public interface AppointmentRequestService {
    List<AppointmentRequest> getAllRequests();

    AppointmentRequest getRequestByUuid(String uuid);

    AppointmentRequest createAppointmentRequest(AppointmentRequestCreationForm requestForm);

    void deleteRequestByUuid(String uuid);
}
