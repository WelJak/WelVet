package com.weljak.welvet.domain.appointmentrequest;

import com.weljak.welvet.domain.animal.Animal;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment_request")
@Builder
@Data
public class AppointmentRequest {

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animalId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "preferred_date", nullable = false)
    private String preferredDate;

    @Column(name = "status", nullable = false)
    private String status;
}
