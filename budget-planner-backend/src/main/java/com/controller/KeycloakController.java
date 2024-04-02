package com.controller;

import com.payload.response.ResponseDto;
import com.security.jwt.JwtUtils;
import com.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keycloak")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@Tag(name = "Keycloak Controller", description = "Endpoints for handling requests for Authentication")
public class KeycloakController {

    private final KeycloakService keycloakService;

    private final JwtUtils jwtUtils;

    @Operation(summary = "Endpoint to get user by Username")
    @GetMapping("/findByUsername")
    public UserRepresentation getKeycloakUserByUsername(@RequestParam String username) {
        return this.keycloakService.findByEmail(username);
    }

    @Operation(summary = "Endpoint to send email")
    @PostMapping("/sendEmail")
    public ResponseEntity<ResponseDto> sendEmail(@RequestParam String email) {
        ResponseDto r = this.keycloakService.sendMailForUpdatingPassword(email);
        return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatusCode()));
    }

    @Operation(summary = "Endpoint to update password")
    @PostMapping("/updatePassword")
    public void updatePassword(@RequestParam String token, @RequestParam String username) {
        this.keycloakService.updatePassword(token, username);
    }

    @Operation(summary = "Endpoint to validate token")
    @PostMapping("/validateToken")
    public boolean validateToken(@RequestParam String token) {
        return this.jwtUtils.validateJwtToken(token);
    }

    @Operation(summary = "Endpoint to send email for verification")
    @PostMapping("/sendVerifyEmail")
    public void sendVerifyEmail(@RequestParam String email) {
        this.keycloakService.sendVerifyEmail(email);
    }

    @Operation(summary = "Endpoint to send email for verification again")
    @PostMapping("/sendVerifyEmailAgain")
    public void sendVerifyEmailAgain(@RequestParam String token) {
        this.keycloakService.sendVerifyEmailAgain(token);
    }

    @Operation(summary = "Endpoint to set email verified")
    @PostMapping("/setEmailVerified")
    public void setEmailVerified(@RequestParam String token) {
        this.keycloakService.setEmailVerified(token);
    }

}
