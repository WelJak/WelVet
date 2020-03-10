package com.weljak.welvet.service.appointments.appointmentrequest;

import com.weljak.welvet.domain.animal.AnimalRepo;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequestRepo;
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
public class AppointmentRequestServiceImpl implements AppointmentRequestService {
    private final AppointmentRequestRepo appointmentRequestRepo;
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
    public AppointmentRequest createAppointmentRequest(AppointmentRequestCreationForm requestForm) {
        log.info("Creating appointment request for animal: {}", requestForm.getAnimalID());
        return AppointmentRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .animalId(animalRepo.findByAnimalId(requestForm.getAnimalID()))
                .preferredDate(LocalDateTime.parse(requestForm.getPreferredDate(), formatter))
                .type(requestForm.getType())
                .build();
    }

    @Override
    public void deleteRequestByUuid(String uuid) {
        log.info("Deleting request with uuid: {}", uuid);
        AppointmentRequest toDelete = appointmentRequestRepo.findByUuid(uuid);
        appointmentRequestRepo.delete(toDelete);
    }
}
