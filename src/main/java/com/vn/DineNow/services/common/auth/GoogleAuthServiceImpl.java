package com.vn.DineNow.services.common.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service implementation for verifying Google ID tokens using Google's OAuth2 APIs.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${DineNow.google.client.id}")
    String GOOGLE_CLIENT_ID;

    /**
     * Verifies a Google ID token and extracts the payload.
     *
     * @param idToken the ID token provided by the client (from Google OAuth2)
     * @return the payload containing user profile information from the token
     * @throws CustomException if the token is invalid
     * @throws Exception for unexpected errors during verification
     */
    @Override
    public GoogleIdToken.Payload verifyToken(String idToken) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idTokenObj = verifier.verify(idToken);

        if (idTokenObj == null) {
            // Token is invalid (expired, tampered, wrong audience...)
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

        return idTokenObj.getPayload(); // Contains user info (email, name, etc.)
    }
}
