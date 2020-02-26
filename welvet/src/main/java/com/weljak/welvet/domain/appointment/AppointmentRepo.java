package com.weljak.welvet.domain.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, String> {
    Appointment findByUuid(String uuid);
    List<Appointment> findAllByDate(String date);
    List<Appointment> findAllByStatus(String status);
    List<Appointment> findAllByType(String type);
}
