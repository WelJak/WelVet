package com.weljak.welvet.webapi.requests;

import lombok.Value;

@Value
public class CreateAnimalRequest {
    private String name;
    private String type;
    private String breed;
    private Integer age;
}
