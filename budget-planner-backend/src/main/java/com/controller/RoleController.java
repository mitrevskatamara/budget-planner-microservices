package com.controller;

import com.model.Role;
import com.service.RoleService;
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
@RequestMapping("/api/roles")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Role Controller", description = "Endpoints for handling requests for Role")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Endpoint to create new Role")
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestParam String name) {
        Role role = this.roleService.createRole(name);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get all Roles")
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = this.roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}