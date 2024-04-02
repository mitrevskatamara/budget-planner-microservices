package com.service;

import com.model.Role;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.RoleRepository;
import com.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    private static final String ROLE = "ROLE_ADMIN";

    private static final long ID = 1L;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = Role.builder().name(ROLE).build();
    }

    @Test
    public void getRoleByIdTest() {
        // given
        when(roleRepository.findById(ID)).thenReturn(Optional.of(role));

        // when
        Role roleFromDb = roleService.getRole(ID);

        // then
        verify(roleRepository).findById(any());
        assertThat(roleFromDb).isNotNull();
    }

    @Test
    public void getRoleByIdUnsuccessfullyTest() {
        // given
        when(roleRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("Role", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> roleService.getRole(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Role not found with id : '1'");
    }

    @Test
    public void createRoleTest() {
        // given
        when(roleRepository.save(role)).thenReturn(role);

        // when
        Role savedRole = roleService.createRole(ROLE);

        // then
        verify(roleRepository).save(savedRole);
        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(ROLE);
    }
}