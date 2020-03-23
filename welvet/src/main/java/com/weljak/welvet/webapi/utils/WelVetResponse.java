package com.weljak.welvet.webapi.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WelVetResponse {
    private String timeStamp;
    private String path;
    private int responseCode;
    private String status;
    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;
}
