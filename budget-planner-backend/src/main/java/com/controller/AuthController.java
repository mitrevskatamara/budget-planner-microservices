package com.controller;

import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.ResponseDto;
import com.service.AuthService;
import com.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Endpoints for handling requests for Authentication")
public class AuthController {

    private final AuthService authService;

    private final KeycloakService keycloakService;

    @Operation(summary = "Endpoint to authenticate user")
    @PostMapping("/loginJwt")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        ResponseDto responseDto = this.authService.login(loginDto);

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }

    @Operation(summary = "Endpoint to register new user")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        ResponseDto responseDto = this.authService.register(signUpDto);

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }

    @Operation(summary = "Endpoint for logout")
    @PostMapping("/logout")
    public ResponseDto logout(HttpServletRequest request) throws ServletException {
        return this.authService.logout(request);
    }

    @Operation(summary = "Endpoint to get current user")
    @GetMapping("/currentUser")
    public ResponseDto getCurrentUser(Authentication authentication) {
        return this.authService.getCurrentUser(authentication);
    }

    @Operation(summary = "Endpoint to login via Keycloak")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@NotNull @RequestBody LoginDto loginDto) {
        ResponseDto responseDto = this.keycloakService.authenticateKeycloakUser(loginDto);
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }

}