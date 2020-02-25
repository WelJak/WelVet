package com.weljak.welvet.domain.appointment;

import com.weljak.welvet.domain.animal.Animal;
import com.weljak.welvet.domain.vet.Vet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "appointment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Appointment {

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @OneToOne(targetEntity = Animal.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private String animalId;

    @OneToOne(targetEntity = Vet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id", nullable = false)
    private String vetId;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "type", nullable = false)
    private String type;
}
