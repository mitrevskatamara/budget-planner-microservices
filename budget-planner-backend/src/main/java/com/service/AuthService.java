package com.service;

import com.payload.request.LoginDto;
import com.payload.request.SignUpDto;
import com.payload.response.ResponseDto;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    ResponseDto register(SignUpDto signUpDto);

    ResponseDto login(LoginDto loginDto);

    ResponseDto logout(HttpServletRequest request) throws ServletException;

    ResponseDto getCurrentUser(Authentication authentication);
}
