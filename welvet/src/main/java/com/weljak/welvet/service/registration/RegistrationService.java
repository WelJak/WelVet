package com.weljak.welvet.service.registration;

import com.weljak.welvet.domain.registration.RegistrationRequest;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.RegistrationConfirmation;

public interface RegistrationService {
    RegistrationRequest createRegistrationRequest(CreateOwnerRequest createOwnerRequest);

    RegistrationConfirmation confirmRegistrationRequest(String registrationRequestId, String confirmationId);
}
