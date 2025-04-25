package com.vn.DineNow.services.common.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleAuthService {
    GoogleIdToken.Payload verifyToken(String idToken) throws Exception;
}