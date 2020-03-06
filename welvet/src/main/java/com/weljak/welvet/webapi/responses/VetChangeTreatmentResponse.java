package com.weljak.welvet.webapi.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VetChangeTreatmentResponse {
    private String animalId;
    private String animalName;
    private String owner;
    private String newTreatment;
}
