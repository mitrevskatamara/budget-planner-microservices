package com.model.dto;

import com.model.Role;
import com.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<String> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles().stream().map(Role::getName).toList();
    }
}