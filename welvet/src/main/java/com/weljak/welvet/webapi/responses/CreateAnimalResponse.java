package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class CreateAnimalResponse {
    private String animalId;
    private String ownerUUID;
    private String name;
    private String type;
    private String breed;
    private Integer age;
}
