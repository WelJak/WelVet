package com.weljak.welvet.service.registration;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.domain.owner.OwnerAlreadyExistsException;
import com.weljak.welvet.domain.owner.OwnerRepo;
import com.weljak.welvet.domain.registration.RegistrationConfirmationIdDoesNotMatchException;
import com.weljak.welvet.domain.registration.RegistrationRequest;
import com.weljak.welvet.domain.registration.RegistrationRequestAlreadyExistsException;
import com.weljak.welvet.domain.registration.RegistrationRequestRepo;
import com.weljak.welvet.service.owner.OwnerService;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.RegistrationConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRequestRepo registrationRequestRepo;
    private final OwnerRepo ownerRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OwnerService ownerService;

    @Override
    public RegistrationRequest createRegistrationRequest(CreateOwnerRequest createOwnerRequest) {
        log.info("Creating new registration request");
        if (registrationRequestRepo.existsByUsername(createOwnerRequest.getUsername())) {
            throw new RegistrationRequestAlreadyExistsException();
        } else if (ownerRepo.existsByUsername(createOwnerRequest.getUsername())) {
            throw new OwnerAlreadyExistsException();
        }
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .confirmationId(UUID.randomUUID().toString())
                .username(createOwnerRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(createOwnerRequest.getPassword()))
                .build();
        registrationRequestRepo.save(registrationRequest);
        return registrationRequest;
    }

    @Override
    public RegistrationConfirmation confirmRegistrationRequest(String registrationRequestId, String confirmationId) {
        log.info("Confirming registration request with id: {}, confirmation id: {} and creating new owner", registrationRequestId, confirmationId);
        RegistrationRequest toConfirm = registrationRequestRepo.getByUuid(registrationRequestId);
        if (confirmationId.equals(toConfirm.getConfirmationId())) {
            Owner newOwner = ownerService.createOwner(toConfirm.getUsername(), toConfirm.getPassword());
            ownerRepo.save(newOwner);
            registrationRequestRepo.delete(toConfirm);
            return new RegistrationConfirmation(
                    toConfirm.getUuid(),
                    toConfirm.getConfirmationId(),
                    newOwner.getUsername()
            );
        } else {
            throw new RegistrationConfirmationIdDoesNotMatchException();
        }
    }
}
