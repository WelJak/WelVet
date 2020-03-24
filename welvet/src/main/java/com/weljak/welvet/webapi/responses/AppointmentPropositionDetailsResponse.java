package com.weljak.welvet.webapi.responses;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AppointmentPropositionDetailsResponse {
    private String uuid;
    private String requestId;
    private String vetId;
    private LocalDateTime newDate;
}
