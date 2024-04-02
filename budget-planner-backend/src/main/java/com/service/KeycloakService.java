package com.service;

import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.ResponseDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

    Response createKeycloakUser(SignUpDto signUpDto);

    ResponseDto authenticateKeycloakUser(LoginDto loginDto);

    UserRepresentation findByEmail(String email);

    UserRepresentation findByUsername(String username);

    ResponseDto sendMailForUpdatingPassword(String email);

    void updatePassword(String usedId, String password);

    void sendVerifyEmail(String email);

    void sendVerifyEmailAgain(String token);

    void setEmailVerified(String email);
    
}
