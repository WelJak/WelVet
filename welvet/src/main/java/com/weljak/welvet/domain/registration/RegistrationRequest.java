package com.weljak.welvet.domain.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registration_request")
@Builder
@Data
public class RegistrationRequest {

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "confirmation_id", nullable = false)
    private String confirmationId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
}
