package com.weljak.welvet.domain.animal;

import com.weljak.welvet.domain.owner.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animals")
@Builder
@Data
public class Animal {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid", nullable = false)
    private Owner uuid;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "treatment", nullable = false)
    private String treatment;

}
