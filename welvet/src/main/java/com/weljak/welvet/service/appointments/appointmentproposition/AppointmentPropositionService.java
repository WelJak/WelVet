package com.weljak.welvet.service.appointments.appointmentproposition;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentproposition.AppointmentProposition;

import java.util.List;

public interface AppointmentPropositionService {
    AppointmentProposition getAppointmentPropositionByUuid(String uuid);

    Appointment confirmAppointmentProposition(String uuid);

    void deleteAppointmentProposition(String uuid);

    List<AppointmentProposition> getAllAppointmentPropositions();
}
