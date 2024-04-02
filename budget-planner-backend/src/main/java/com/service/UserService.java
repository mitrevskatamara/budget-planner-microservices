package com.service;


import com.model.User;
import com.model.dto.RoleDto;
import com.model.dto.UserDto;
import com.payload.request.SignUpDto;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User register(SignUpDto signUpDto);

    User update(Long id, UserDto userDto);

    User changeProfileStatus(Long id);

    User findByUsername(String username);

    User changeUserRole(RoleDto roleDto);

    User changePassword(String username, String newPassword);

    void sendEmail(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByUsernameOrEmail(String emailOrUsername);
}
