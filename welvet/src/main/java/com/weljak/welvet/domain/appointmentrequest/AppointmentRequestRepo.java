package com.weljak.welvet.domain.appointmentrequest;

import com.weljak.welvet.domain.owner.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRequestRepo extends JpaRepository<AppointmentRequest, String> {
    AppointmentRequest findByUuid(String uuid);

    List<AppointmentRequest> findAllByOwnerId(Owner owner);
}
