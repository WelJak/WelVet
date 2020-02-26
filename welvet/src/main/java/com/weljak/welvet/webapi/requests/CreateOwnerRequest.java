package com.weljak.welvet.webapi.requests;

import lombok.Value;

@Value
public class CreateOwnerRequest {
    private String username;
    private String password;
}
