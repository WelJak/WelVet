package com.weljak.welvet.domain.appointmentproposition;

import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentPropositionRepo extends JpaRepository<AppointmentProposition, String> {
    AppointmentProposition findByUuid(String uuid);

    AppointmentProposition findByRequest(AppointmentRequest request);
}
