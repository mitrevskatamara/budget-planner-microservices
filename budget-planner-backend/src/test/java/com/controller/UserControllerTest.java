package com.controller;

import com.model.User;
import com.model.dto.RoleDto;
import com.model.dto.UserDto;
import com.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
public class UserControllerTest {

    public static final long ID = 1L;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    
    private User user;
    
    @BeforeEach
    public void setup() {
        user = User.builder().build();
    }

    @Test
    public void getAllUsersTest() {
        // given
        List<User> users = new ArrayList<>();
        when(userService.getAll()).thenReturn(users);

        // when
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserTest() {
        // given
        when(userService.getById(ID)).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.getUser(ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateUserTest() {
        // given
        UserDto userDto = UserDto.builder().build();
        when(userService.update(ID, userDto)).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.updateUser(ID, userDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void changeProfileStatusTest() {
        // given
        when(userService.changeProfileStatus(1L)).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.changeProfileStatus(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void changeUserRolesTest() {
        // given
        RoleDto roleDto = RoleDto.builder().build();
        when(userService.changeUserRole(roleDto)).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.changeUserRoles(roleDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void findByUsernameTest() {
        // given
        when(userService.findByUsername("")).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.findByUsername("");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}