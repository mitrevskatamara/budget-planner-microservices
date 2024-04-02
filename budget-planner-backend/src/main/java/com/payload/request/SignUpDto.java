package com.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDto {

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;

    private String confirmedPassword;
}
