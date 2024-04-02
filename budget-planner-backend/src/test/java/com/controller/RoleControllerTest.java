package com.controller;

import com.model.Role;
import com.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    public void createRoleTest() {
        // given
        Role role = Role.builder().build();
        when(roleService.createRole("")).thenReturn(role);

        // when
        ResponseEntity<Role> response = roleController.createRole("");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAllRolesTest() {
        // given
        List<Role> roles = new ArrayList<>();
        when(roleService.getAllRoles()).thenReturn(roles);

        // when
        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
