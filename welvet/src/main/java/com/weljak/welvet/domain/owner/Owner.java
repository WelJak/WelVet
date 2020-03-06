package com.weljak.welvet.domain.owner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weljak.welvet.domain.vet.VetInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "owner")
@Builder
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner {

    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany
    @JoinColumn(name = "uuid")
    private List<VetInfo> vetInfo;

//    @ManyToOne(targetEntity = Animal.class)
//    @JoinColumn(name = "owned_animals")
//    //@Column(name = "owned_animals")
//    private List<Animal> ownedAnimals;

}
