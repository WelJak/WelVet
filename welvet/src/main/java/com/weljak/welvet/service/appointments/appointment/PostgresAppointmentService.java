package com.weljak.welvet.service.appointments.appointment;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.appointment.*;
import com.weljak.welvet.domain.owner.Owner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresAppointmentService implements AppointmentService {
    private final AppointmentRepo appointmentRepo;

    @Override
    public Appointment createAppointment(Animal animal, Owner vet, LocalDateTime date, String type) {
        log.info("Creating new appointment for animal: {}", animal.getAnimalId());
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
        log.info("Getting details for appointment with id: {}", uuid);
        return appointmentRepo.findByUuid(uuid);
    }

    @Override
    public Appointment changeAppointmentStatus(String uuid, AppointmentStatus status) {
        log.info("Changing appointment status for appointment with id: {}, status: {}", uuid, status);
        Appointment appointment = appointmentRepo.findByUuid(uuid);
        switch (appointment.getStatus()) {
            case COMPLETED:
                throw new AppointmentAlreadyCompletedException();
            case POSTPONED:
                throw new AppointmentAlreadyPostponedException();
            case CANCELED:
                throw new AppointmentAlreadyCancelledException();
        }
        appointment.setStatus(status);
        appointmentRepo.save(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    @Override
    public void deleteAppointmentByUuid(String uuid) {
        appointmentRepo.delete(appointmentRepo.findByUuid(uuid));
    }
}
