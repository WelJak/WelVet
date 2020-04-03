package com.weljak.welvet.service.appointments.appointmentrequest;

import com.weljak.welvet.domain.animal.AnimalRepo;
import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequestRepo;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.service.appointments.appointment.AppointmentService;
import com.weljak.welvet.webapi.requests.AppointmentRequestCreationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresAppointmentRequestService implements AppointmentRequestService {
    private final AppointmentRequestRepo appointmentRequestRepo;
    private final AppointmentService appointmentService;
    private final AnimalRepo animalRepo;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<AppointmentRequest> getAllRequests() {
        log.info("Fetching all pending requests.");
        return appointmentRequestRepo.findAll();
    }

    @Override
    public AppointmentRequest getRequestByUuid(String uuid) {
        log.info("Getting details of request with uuid: {}", uuid);
        return appointmentRequestRepo.findByUuid(uuid);
    }

    @Override
    public Appointment confirmAppointmentRequest(Owner vet, String requestId) {
        log.info("Confirming appointment request: {}", requestId);
        AppointmentRequest request = appointmentRequestRepo.findByUuid(requestId);
        Appointment appointment = appointmentService.createAppointment(
                request.getAnimalId(),
                vet,
                request.getPreferredDate(),
                request.getType()
        );
        log.info("Deleting confirmed request: {}", requestId);
        appointmentRequestRepo.delete(request);
        return appointment;
    }

    @Override
    public AppointmentRequest createAppointmentRequest(AppointmentRequestCreationForm requestForm, Owner owner) {
        log.info("Creating appointment request for animal: {}", requestForm.getAnimalID());
        AppointmentRequest request = AppointmentRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .animalId(animalRepo.findByAnimalId(requestForm.getAnimalID()))
                .ownerId(owner)
                .preferredDate(LocalDateTime.parse(requestForm.getPreferredDate(), formatter))
                .type(requestForm.getType())
                .build();
        appointmentRequestRepo.save(request);
        return request;
    }

    @Override
    public void deleteRequestByUuid(String uuid) {
        log.info("Deleting request with uuid: {}", uuid);
        AppointmentRequest toDelete = appointmentRequestRepo.findByUuid(uuid);
        appointmentRequestRepo.delete(toDelete);
    }

    @Override
    public List<AppointmentRequest> getOwnersRequests(Owner owner) {
        log.info("Geting all pending requests for user: {}", owner.getUuid());
        return appointmentRequestRepo.findAllByOwnerId(owner);
    }
}
