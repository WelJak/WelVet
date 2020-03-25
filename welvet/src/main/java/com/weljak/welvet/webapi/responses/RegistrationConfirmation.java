package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class RegistrationConfirmation {
    private String registrationRequestId;
    private String registrationConfirmationId;
    private String username;

}
