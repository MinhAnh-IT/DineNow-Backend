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

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${DineNow.google.client.id}")
    String GOOGLE_CLIENT_ID;

    public GoogleIdToken.Payload verifyToken(String idToken) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idTokenObj = verifier.verify(idToken);
        if (idTokenObj == null) {
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

        return idTokenObj.getPayload();
    }
}
