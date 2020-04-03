package com.weljak.welvet.domain.vet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "vet_info",
        indexes = {@Index(columnList = "uuid", name = "vet_info_uuid")}
)
@Data
@Builder
public class VetInfo {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "specialization")
    private String specialization;
}
