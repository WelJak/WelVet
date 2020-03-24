package com.weljak.welvet.service.appointments.appointmentproposition;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentproposition.AppointmentProposition;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.owner.Owner;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentPropositionService {
    AppointmentProposition createAppointmentProposition(Owner vet, String appointmentRequestId, LocalDateTime newDate);

    AppointmentProposition getAppointmentPropositionByUuid(String uuid);

    Appointment confirmAppointmentProposition(String uuid);

    void deleteAppointmentProposition(String uuid);

    List<AppointmentProposition> getAllAppointmentPropositions();
}
