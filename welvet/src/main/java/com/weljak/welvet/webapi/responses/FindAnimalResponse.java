package com.weljak.welvet.webapi.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindAnimalResponse {
    private String animalId;
    private String name;
    private String type;
    private String breed;
    private Integer age;
    private String treatment;
    private String owner;
}
