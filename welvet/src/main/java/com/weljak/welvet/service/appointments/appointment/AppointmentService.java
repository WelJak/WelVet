package com.weljak.welvet.service.appointments.appointment;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointment.AppointmentStatus;
import com.weljak.welvet.domain.owner.Owner;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Animal animal, Owner vet, LocalDateTime date, String type);

    Appointment getAppointmentByUuid(String uuid);

    Appointment changeAppointmentStatus(String uuid, AppointmentStatus status);

    List<Appointment> getAllAppointments();

    void deleteAppointmentByUuid(String uuid);
}
