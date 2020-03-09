package com.weljak.welvet.service.appointments.appointment;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.owner.Owner;

public interface AppointmentService {
    Appointment createAppointment(Animal animal, Owner vet, String date, String type);
}
