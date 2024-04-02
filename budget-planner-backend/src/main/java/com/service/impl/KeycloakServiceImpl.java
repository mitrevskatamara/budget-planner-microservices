package com.service.impl;

import com.config.KeycloakConfig;
import com.model.User;
import com.model.dto.UserDto;
import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.ResponseDto;
import com.security.jwt.JwtUtils;
import com.service.KeycloakService;
import com.service.UserService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.utils.Constants.*;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    public static final String UPDATE_PASSWORD = "UPDATE_PASSWORD";

    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakConfig keycloakConfig;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private UsersResource getUsersResource() {
        return keycloakConfig.keycloak().realm(realm).users();
    }

    public KeycloakServiceImpl(KeycloakConfig keycloakConfig, UserService userService, JwtUtils jwtUtils) {
        this.keycloakConfig = keycloakConfig;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Response createKeycloakUser(SignUpDto signUpDto) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(signUpDto.getPassword());
        UserRepresentation kcUser = createUserRepresentation(signUpDto, credentialRepresentation);

        Response response = getUsersResource().create(kcUser);

        return response;
    }

    @Override
    public ResponseDto authenticateKeycloakUser(LoginDto loginDto) {
        ResponseDto responseDto;
        UserRepresentation userByEmail = this.findByEmail(loginDto.getUsername());
        UserRepresentation userByUsername = this.findByUsername(loginDto.getUsername());

        if (userByEmail != null && !userByEmail.isEmailVerified()) {
            responseDto = setValuesToResponseDto(404, EMAIL_NOT_VERIFIED, null, null);
        } else {
            Keycloak keycloak = keycloakConfig.initializeKeycloakWithCredentials(loginDto.getUsername(), loginDto.getPassword());
            AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();

            responseDto = setValuesToResponseDto(200, SUCCESSFUL_LOGIN, loginDto.getUsername(), accessTokenResponse);
        }

        return responseDto;
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        List<UserRepresentation> list = getUsersResource().searchByEmail(email, true);
        Optional<UserRepresentation> user = list.stream().findFirst();

        return user.orElse(null);
    }

    @Override
    public UserRepresentation findByUsername(String username) {
        List<UserRepresentation> list = getUsersResource().searchByUsername(username, true);
        Optional<UserRepresentation> user = list.stream().findFirst();

        return user.orElse(null);
    }

    @Override
    public ResponseDto sendMailForUpdatingPassword(String email) {
        UserRepresentation user = this.findByEmail(email);
        ResponseDto responseDto;
        if (user != null) {
            UserResource u = getUsersResource().get(user.getId());
            u.executeActionsEmail(List.of(UPDATE_PASSWORD));
            responseDto = setValuesToResponseDto(200, SENT_EMAIL, null, null);
        } else {
            responseDto = setValuesToResponseDto(404, NOT_FOUND_USER_BY_EMAIl_MESSAGE, null, null);
        }
        return responseDto;
    }

    @Override
    public void updatePassword(String userId, String password) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(password);
        String email = jwtUtils.getEmailFromToken(userId);

        UserRepresentation user = this.findByEmail(email);
        user.setCredentials(Collections.singletonList(credentialRepresentation));
        UserResource userResource = getUsersResource().get(user.getId());

        userResource.update(user);
    }

    @Override
    public void sendVerifyEmail(String email) {
        UserRepresentation user = this.findByEmail(email);
        getUsersResource().get(user.getId()).sendVerifyEmail();
    }

    @Override
    public void sendVerifyEmailAgain(String token) {
        String email = jwtUtils.getEmailFromToken(token);
        this.sendVerifyEmail(email);
    }

    @Override
    public void setEmailVerified(String token) {
        String email = jwtUtils.getEmailFromToken(token);
        UserRepresentation user = this.findByEmail(email);
        user.setEmailVerified(true);
        getUsersResource().get(user.getId()).update(user);
    }

    private UserRepresentation createUserRepresentation(SignUpDto user, CredentialRepresentation credentialRepresentation) {
        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setGroups(new ArrayList<>());

        return kcUser;
    }


    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();

        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }

    private ResponseDto setValuesToResponseDto(Integer statusCode, String message, String username, AccessTokenResponse token) {
        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatusCode(statusCode);
        responseDto.setMessage(message);

        if (username != null) {
            User user = this.userService.findByUsername(username);
            responseDto.setUser(new UserDto(user));
        } else {
            responseDto.setUser(null);
        }

        responseDto.setAccessTokenResponse(token);

        return responseDto;
    }
}
