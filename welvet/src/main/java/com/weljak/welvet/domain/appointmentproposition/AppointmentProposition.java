package com.weljak.welvet.domain.appointmentproposition;

import com.weljak.welvet.domain.appointmentrequest.AppointmentRequest;
import com.weljak.welvet.domain.owner.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment_proposition")
@Builder
@Data
public class AppointmentProposition {

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request", nullable = false)
    private AppointmentRequest request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vet", nullable = false)
    private Owner vet;

    @Column(name = "new_date", nullable = false)
    private LocalDateTime newDate;
}
