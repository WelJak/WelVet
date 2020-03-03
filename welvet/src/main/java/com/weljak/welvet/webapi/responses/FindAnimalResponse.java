package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class FindAnimalResponse {
    private String animalId;
    private String name;
    private String type;
    private String breed;
    private Integer age;
    private String treatment;
}
