package com.weljak.welvet.service.appointments.appointmentrequest;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.webapi.requests.AppointmentRequestCreationForm;

import java.util.List;

public interface AppointmentRequestService {
    List<AppointmentRequest> getAllRequests();

    AppointmentRequest getRequestByUuid(String uuid);

    AppointmentRequest createAppointmentRequest(AppointmentRequestCreationForm requestForm, Owner owner);

    void deleteRequestByUuid(String uuid);

    Appointment confirmAppointmentRequest(Owner vet, String requestId);

    List<AppointmentRequest> getOwnersRequests(Owner owner);
}
