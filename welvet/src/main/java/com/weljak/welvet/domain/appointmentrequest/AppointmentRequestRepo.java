package com.weljak.welvet.domain.appointmentrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRequestRepo extends JpaRepository<AppointmentRequest, String> {
    AppointmentRequest findByUuid(String uuid);
    AppointmentRequest findAllByStatus(String status);
}
