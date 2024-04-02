package com.service.impl;

import com.model.Role;
import com.model.User;
import com.model.dto.RoleDto;
import com.model.dto.UserDto;
import com.model.exceptions.ResourceNotFoundException;
import com.payload.request.SignUpDto;
import com.repository.UserRepository;
import com.service.EmailService;
import com.service.RoleService;
import com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final EmailService emailService;

    private static final String ROLE_USER = "ROLE_USER";

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", id.toString()));
    }

    @Override
    public User register(SignUpDto signUpRequest) {
        Role role = this.roleService.getRoleByName(ROLE_USER);

        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .username(signUpRequest.getUsername())
                .roles(Collections.singleton(role))
                .status(true)
                .build();

        return this.userRepository.save(user);
    }

    @Override
    public User update(Long id, UserDto userDto) {
        User user = this.getById(id);
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();

        if (Objects.nonNull(firstName)) {
            user.setFirstName(firstName);
        }
        if (Objects.nonNull(lastName)) {
            user.setLastName(lastName);
        }

        return this.userRepository.save(user);
    }

    @Override
    public User changeProfileStatus(Long id) {
        User user = this.getById(id);

        user.setStatus(!user.getStatus());

        return this.userRepository.save(user);
    }

    @Override
    public User changeUserRole(RoleDto roleDto) {
        User user = this.getById(roleDto.getUserId());
        Role role = this.roleService.getRoleByName(roleDto.getRoleName());

        Set<Role> roles = user.getRoles();
        if (roles.contains(role)) {
            roles.remove(role);
        } else {
            roles.add(role);
        }

        user.setRoles(roles);

        return this.userRepository.save(user);
    }

    @Override
    public User changePassword(String username, String newPassword) {
        User user = this.findByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));

        return this.userRepository.save(user);
    }

    @Override
    public void sendEmail(String username) {
        User user = this.findByUsername(username);

        emailService.sendEmail(user.getEmail(),  "");
    }

    @Override
    public Boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User findByUsernameOrEmail(String emailOrUsername) {
        return this.userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", emailOrUsername));
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));
    }
}