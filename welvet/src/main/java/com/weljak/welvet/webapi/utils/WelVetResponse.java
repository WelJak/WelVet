package com.weljak.welvet.webapi.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class WelVetResponse {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;
}
