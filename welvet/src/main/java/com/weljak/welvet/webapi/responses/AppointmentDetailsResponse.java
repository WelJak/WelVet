package com.weljak.welvet.webapi.responses;

import com.weljak.welvet.domain.appointment.AppointmentStatus;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AppointmentDetailsResponse {
    private String uuid;
    private String animalId;
    private String vetId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String type;
}
