package com.weljak.welvet.webapi.requests;

import lombok.Value;

@Value
public class VetChangeAnimalTreatmentRequest {
    private String animalId;
    private String treatment;
}
