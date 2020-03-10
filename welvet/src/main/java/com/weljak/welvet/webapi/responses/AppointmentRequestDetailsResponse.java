package com.weljak.welvet.webapi.responses;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AppointmentRequestDetailsResponse {
    private String uuid;
    private String animalId;
    private String type;
    private LocalDateTime preferredDate;
}
