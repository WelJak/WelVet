package com.weljak.welvet.webapi.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VetGetOwnerDetailsResponse {
    private String uuid;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> animalsOwned;
}
