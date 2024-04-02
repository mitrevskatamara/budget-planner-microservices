package com.payload.response;

import com.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private Integer statusCode;

    private String message;

    private UserDto user;

    private JwtResponseDto jwtResponse;

    private AccessTokenResponse accessTokenResponse;
}
