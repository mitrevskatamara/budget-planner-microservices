package com.controller;

import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.ResponseDto;
import com.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ResponseDto responseDto;

    @BeforeEach
    public void setup() {
        responseDto = ResponseDto.builder().statusCode(200).build();
    }

    @Test
    public void authenticateUserTest() {
        // given
        LoginDto loginDto = LoginDto.builder().build();
        when(authService.login(loginDto)).thenReturn(responseDto);

        // when
        ResponseEntity<?> response = authController.authenticateUser(loginDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void registerUserTest() {
        // given
        SignUpDto signUpDto = SignUpDto.builder().build();
        when(authService.register(signUpDto)).thenReturn(responseDto);

        // when
        ResponseEntity<?> response = authController.registerUser(signUpDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
