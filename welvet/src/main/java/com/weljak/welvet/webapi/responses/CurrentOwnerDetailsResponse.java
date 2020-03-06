package com.weljak.welvet.webapi.responses;

import lombok.Value;

@Value
public class CurrentOwnerDetailsResponse {
    private String uuid;
    private String username;
    private String role;
}
