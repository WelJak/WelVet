package com.weljak.welvet.service.appointments.appointmentproposition;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentproposition.AppointmentProposition;
import com.weljak.welvet.domain.appointmentproposition.AppointmentPropositionRepo;
import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.service.appointments.appointmentrequest.AppointmentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentPropositionServiceImpl implements AppointmentPropositionService {
    private final AppointmentPropositionRepo appointmentPropositionRepo;
    private final AppointmentRequestService appointmentRequestService;

    @Override
    public AppointmentProposition createAppointmentProposition(Owner vet, String appointmentRequestId, LocalDateTime newDate) {
        log.info("Creating new appointment proposition for appointment request with id: {}", appointmentRequestId);
        AppointmentProposition appointmentProposition = AppointmentProposition.builder()
                .uuid(UUID.randomUUID().toString())
                .request(appointmentRequestService.getRequestByUuid(appointmentRequestId))
                .vet(vet)
                .newDate(newDate)
                .build();
        appointmentPropositionRepo.save(appointmentProposition);
        return appointmentProposition;
    }

    @Override
    public AppointmentProposition getAppointmentPropositionByUuid(String uuid) {
        log.info("Fetching appointment proposition details with id: {}", uuid);
        return appointmentPropositionRepo.findByUuid(uuid);
    }

    @Override
    public Appointment confirmAppointmentProposition(String uuid) {
        log.info("Confirming appointment proposition with id: {}", uuid);
        AppointmentProposition toConfirm = appointmentPropositionRepo.findByUuid(uuid);
        appointmentPropositionRepo.delete(toConfirm);
        Appointment appointment = appointmentRequestService.confirmAppointmentRequest(toConfirm.getVet(), toConfirm.getRequest().getUuid());
        return appointment;
    }

    @Override
    public void deleteAppointmentProposition(String uuid) {
        log.info("Deleting appointment proposition: {}", uuid);
        appointmentPropositionRepo.delete(appointmentPropositionRepo.findByUuid(uuid));
    }

    @Override
    public List<AppointmentProposition> getAllAppointmentPropositions() {
        log.info("Fetching all pending appointment propositions");
        return appointmentPropositionRepo.findAll();
    }
}
