package com.service;

import com.model.Role;
import com.model.User;
import com.model.dto.UserDto;
import com.model.exceptions.ResourceNotFoundException;
import com.payload.request.SignUpDto;
import com.repository.UserRepository;
import com.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String USERNAME = "username";
    private static final long ID = 1L;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().id(ID).username(USERNAME).status(true).build();
    }

    @Test
    public void getAllUsersTest() {
        // given
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        // when
        List<User> users = userService.getAll();

        // then
        verify(userRepository).findAll();
        assertThat(users).isNotNull();
    }

    @Test
    public void getUserByIdSuccessfullyTest() {
        // given
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        // when
        User userFromDb = userService.getById(ID);

        // then
        verify(userRepository).findById(any());
        assertThat(userFromDb).isNotNull();
        assertThat(userFromDb).isEqualTo(user);
    }

    @Test
    public void getUserByIdUnsuccessfullyTest() {
        // given
        when(userRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("User", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> userService.getById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("User not found with id : '1'");
    }

    @Test
    public void registerUserSuccessfullyTest() {
        // given
        SignUpDto signUpRequest = SignUpDto.builder().password("pas").confirmedPassword("pas").build();
        Role role = Role.builder().build();
        when(roleService.getRoleByName("ROLE_USER")).thenReturn(role);
        User newUser = User.builder().roles(Collections.singleton(role)).status(true).build();
        User savedUser = User.builder().roles(Collections.singleton(role)).status(true).build();
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // when

        User userSavedInDb = userService.register(signUpRequest);

        // then
        assertThat(userSavedInDb).isNotNull();
        assertThat(userSavedInDb).isEqualTo(newUser);
    }

    @Test
    public void updateUserTest() {
        // given
        UserDto userDto = UserDto.builder().build();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // when
        User updatedUser = userService.update(ID, userDto);

        // then
        verify(userRepository).findById(any());
        verify(userRepository).save(user);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).isEqualTo(user);

    }

    @Test
    public void changeProfileStatusTest() {
        // given
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // when
        User deletedUser = userService.changeProfileStatus(ID);

        // then
        verify(userRepository).save(user);
        assertThat(deletedUser).isNotNull();
        assertThat(deletedUser).isEqualTo(user);
    }

    @Test
    public void deleteUserThrowExceptionTest() {
        // given
        when(userRepository.findById(any())).thenThrow(ResourceNotFoundException.class);

        // when

        // then
        assertThatThrownBy(() -> userService.getById(any()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void findUserByUsernameSuccessfullyTest() {
        // given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        // when
        User userFromDb = userService.findByUsername(USERNAME);

        // then
        verify(userRepository).findByUsername(anyString());
        assertThat(userFromDb).isNotNull();
        assertThat(userFromDb).isEqualTo(user);
    }

    @Test
    public void findUserByUsernameUnsuccessfullyTest() {
        // given
        when(userRepository.findByUsername(USERNAME))
                .thenThrow(new ResourceNotFoundException("User", USERNAME, USERNAME));

        // when

        // then
        assertThatThrownBy(() -> userService.findByUsername(USERNAME))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("User not found with username : 'username'");
    }
}
