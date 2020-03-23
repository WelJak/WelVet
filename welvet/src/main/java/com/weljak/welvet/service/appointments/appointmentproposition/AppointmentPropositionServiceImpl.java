package com.weljak.welvet.service.appointments.appointmentproposition;

import com.weljak.welvet.domain.appointment.Appointment;
import com.weljak.welvet.domain.appointmentproposition.AppointmentProposition;
import com.weljak.welvet.domain.appointmentproposition.AppointmentPropositionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentPropositionServiceImpl implements AppointmentPropositionService {
    private final AppointmentPropositionRepo appointmentPropositionRepo;

    @Override
    public AppointmentProposition getAppointmentPropositionByUuid(String uuid) {
        log.info("Fetching appointment proposition details with id: {}", uuid);
        return appointmentPropositionRepo.findByUuid(uuid);
    }

    @Override
    public Appointment confirmAppointmentProposition(String uuid) {
        return null;
    }

    @Override
    public void deleteAppointmentProposition(String uuid) {

    }

    @Override
    public List<AppointmentProposition> getAllAppointmentPropositions() {
        return null;
    }
}
