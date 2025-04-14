package com.vn.DineNow.services.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface IGoogleAuthService {
    GoogleIdToken.Payload verifyToken(String idToken) throws Exception;
}