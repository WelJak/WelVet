package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class CreateOwnerResponse {
    private String uuid;
    private String username;
}
