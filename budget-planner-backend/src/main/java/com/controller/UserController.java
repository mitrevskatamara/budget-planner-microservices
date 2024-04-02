package com.controller;

import com.model.User;
import com.model.dto.RoleDto;
import com.model.dto.UserDto;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:3000")
@Tag(name = "User Controller", description = "Endpoints for handling requests for Users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Endpoint to get all created Users")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.getAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get User by id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = this.userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to update existing User")
    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = this.userService.update(id, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to change profile status")
    @PostMapping("/status/{id}")
    public ResponseEntity<User> changeProfileStatus(@PathVariable Long id) {
        User user = this.userService.changeProfileStatus(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to change user role")
    @PostMapping("/role")
    public ResponseEntity<User> changeUserRoles(@RequestBody RoleDto roleDto) {
        User user = this.userService.changeUserRole(roleDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get user by username")
    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = this.userService.findByUsername(username);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}