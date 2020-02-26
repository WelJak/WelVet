package com.weljak.welvet.webapi.responses;

import com.weljak.welvet.domain.animal.Animal;
import lombok.Value;

import java.util.List;

@Value
public class FindOwnerResponse {
    private String uuid;
    private String username;
    private String role;
    private List<Animal> ownedAnimals;
}
