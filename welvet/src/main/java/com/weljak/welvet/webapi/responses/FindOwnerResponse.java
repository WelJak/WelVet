package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class FindOwnerResponse {
    private String uuid;
    private String username;
    private String role;
}
