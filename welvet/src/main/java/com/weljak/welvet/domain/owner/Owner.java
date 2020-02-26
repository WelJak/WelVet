package com.weljak.welvet.domain.owner;

import com.weljak.welvet.domain.animal.Animal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "owner")
@Builder
@Data
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

    @ManyToOne(targetEntity = Animal.class)
    @JoinColumn(name = "owned_animals")
    //@Column(name = "owned_animals")
    private List<Animal> ownedAnimals;

}
