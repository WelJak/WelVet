package com.weljak.welvet.domain.vet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Vet {
    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "role", nullable = false)
    private String role;

}
