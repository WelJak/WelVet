package com.weljak.welvet.webapi.requests;

import lombok.Value;

@Value
public class AppointmentRequestCreationForm {
    private String animalID;
    private String type;
    private String preferredDate;
}
