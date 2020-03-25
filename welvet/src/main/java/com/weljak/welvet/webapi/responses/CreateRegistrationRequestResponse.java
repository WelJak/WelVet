package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class CreateRegistrationRequestResponse {
    private String username;
    private String status = "CREATED";
}
