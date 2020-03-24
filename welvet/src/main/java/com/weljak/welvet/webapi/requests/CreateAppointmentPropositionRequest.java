package com.weljak.welvet.webapi.requests;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CreateAppointmentPropositionRequest {
    private String requestId;
    private LocalDateTime newDate;
}
