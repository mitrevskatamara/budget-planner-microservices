package com.repository;

import com.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().username("username").email("email").build();
        userRepository.save(user);
    }

    @Test
    public void saveUserTest() {
        // given

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(user).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void getUserByIdTest() {
        // given

        // when
        Optional<User> optionalUser = userRepository.findById(user.getId());

        // then
        assertThat(optionalUser).isNotNull();
    }

    @Test
    public void getAllUsersTest() {
        // given
        User user1 = User.builder().build();
        User user2 = User.builder().build();

        userRepository.save(user1);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).isNotNull();
    }

    @Test
    public void updateUserTest() {
        // given

        // when
        User savedUser = userRepository.findById(user.getId()).get();
        savedUser.setUsername("username");
        User updatedUser = userRepository.save(user);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("username");
    }

    @Test
    public void deleteUserTest() {
        // given

        // when
        userRepository.delete(user);
        Optional<User> optionalUser = userRepository.findById(user.getId());

        // then
        assertThat(optionalUser).isEmpty();
    }

    @Test
    public void findUserByUsernameSuccessfullyTest() {
        // given

        // when
        Optional<User> savedUser = userRepository.findByUsername("username");

        // then
        assertThat(savedUser).isNotNull();
    }

    @Test
    public void findUserByUsernameUnsuccessfullyTest() {
        // given

        // when
        Optional<User> savedUser = userRepository.findByUsername("username1");

        // then
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void findUserByUsernameOrEmailSuccessfullyTest() {
        // given

        // when
        Optional<User> savedUser = userRepository.findByEmailOrUsername("email", "username");

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void findUserByUsernameOrEmailUnsuccessfullyTest() {
        // given

        // when
        Optional<User> savedUser = userRepository.findByEmailOrUsername("email1", "username1");

        // then
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void userExistsByUsernameTest() {
        // given

        // when
        Boolean exists = userRepository.existsByUsername("username");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void userDoNotExistsByUsernameTest() {
        // given

        // when
        Boolean exists = userRepository.existsByUsername("username1");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    public void userExistsByEmailTest() {
        // given

        // when
        Boolean exists = userRepository.existsByEmail("email");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void userDoNotExistsByEmailTest() {
        // given

        // when
        Boolean exists = userRepository.existsByEmail("email1");

        // then
        assertThat(exists).isFalse();
    }
}