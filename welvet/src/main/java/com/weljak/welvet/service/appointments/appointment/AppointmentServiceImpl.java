package com.weljak.welvet.service.appointments.appointment;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointment.AppointmentRepo;
import com.weljak.welvet.domain.appointment.AppointmentStatus;
import com.weljak.welvet.domain.owner.Owner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepo appointmentRepo;

    @Override
    public Appointment createAppointment(Animal animal, Owner vet, LocalDateTime date, String type) {
        Appointment appointment = Appointment.builder()
                .uuid(UUID.randomUUID().toString())
                .animalId(animal)
                .vetId(vet)
                .date(date)
                .type(type)
                .status(AppointmentStatus.PENDING)
                .build();
        appointmentRepo.save(appointment);
        return appointment;
    }

    @Override
    public Appointment getAppointmentByUuid(String uuid) {
        return appointmentRepo.findByUuid(uuid);
    }
}
