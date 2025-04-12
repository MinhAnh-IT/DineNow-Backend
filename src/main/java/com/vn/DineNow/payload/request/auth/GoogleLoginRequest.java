package com.vn.DineNow.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    private String idToken;
}
