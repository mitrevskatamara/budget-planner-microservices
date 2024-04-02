package com.service.impl;

import com.model.User;
import com.model.dto.UserDto;
import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.JwtResponseDto;
import com.payload.response.ResponseDto;
import com.security.jwt.JwtUtils;
import com.security.service.UserDetailsImpl;
import com.service.AuthService;
import com.service.KeycloakService;
import com.service.UserService;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static com.utils.Constants.*;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final KeycloakService keycloakService;

    //private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    public ResponseDto register(SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        String username = signUpDto.getUsername();
        ResponseDto response;

        if (this.userService.existsByEmail(email)) {
            response = setValuesToResponseDto(409, USER_EXISTS_BY_EMAIL_MESSAGE, null, null);
        } else if (this.userService.existsByUsername(username)) {
            response = setValuesToResponseDto(409, USER_EXISTS_BY_USERNAME_MESSAGE, null, null);
        } else {
            Response keycloakResponse = this.keycloakService.createKeycloakUser(signUpDto);

            if (keycloakResponse.getStatus() == 201) {
                User user = this.userService.register(signUpDto);
                response = setValuesToResponseDto(200, REGISTER_MESSAGE, user.getUsername(), null);
            } else {
                response = setValuesToResponseDto(400, keycloakResponse.getStatusInfo().getReasonPhrase(), null, null);
            }
        }

        return response;
    }

    @Override
    public ResponseDto login(LoginDto loginDto) {
        String usernameOrEmail = loginDto.getUsername();
        String password = loginDto.getPassword();
        ResponseDto response;

        if (!this.userService.existsByUsername(usernameOrEmail) && !this.userService.existsByEmail(usernameOrEmail)) {
            response = setValuesToResponseDto(404, NOT_FOUND_USER_MESSAGE, null, null);
        } else {
//            Authentication authentication = this.authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = this.jwtUtils.generateJwtToken(authentication);
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//            response = setValuesToResponseDto(200, LOGIN_MESSAGE, userDetails.getUsername(), jwt);
            response = null;
        }

        return response;
    }

    @Override
    public ResponseDto logout(HttpServletRequest request) throws ServletException {
        request.logout();

        ResponseDto response = setValuesToResponseDto(200, LOGOUT_MESSAGE, null, null);

        return response;
    }

    @Override
    public ResponseDto getCurrentUser(Authentication authentication) {
        ResponseDto response;

        if (authentication == null) {
            response = setValuesToResponseDto(404, NO_CURRENT_USER_SIGNED_IN_MESSAGE, null, null);
        } else {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            response = setValuesToResponseDto(200, USER_FOUND_MESSAGE, userDetails.getUsername(), null);
        }

        return response;
    }

    private ResponseDto setValuesToResponseDto(Integer statusCode, String message, String username, String jwt) {
        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatusCode(statusCode);
        responseDto.setMessage(message);

        if (username != null) {
            User user = this.userService.findByUsername(username);
            responseDto.setUser(new UserDto(user));
        } else {
            responseDto.setUser(null);
        }

        if (jwt != null) {
            JwtResponseDto jwtResponse = new JwtResponseDto();
            jwtResponse.setToken(jwt);
            responseDto.setJwtResponse(jwtResponse);
        } else {
            responseDto.setJwtResponse(null);
        }

        return responseDto;
    }

}
