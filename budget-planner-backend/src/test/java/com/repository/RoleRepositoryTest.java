package com.repository;

import com.model.Role;
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
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setup() {
        role = Role.builder().name("ROLE_ADMIN").build();
        roleRepository.save(role);
    }

    @Test
    public void saveRoleTest() {
        // given

        // when
        Role savedRole = roleRepository.save(role);

        // then
        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(role.getName());
    }

    @Test
    public void getAllRolesTest() {
        // given
        Role role_user = Role.builder().name("ROLE_USER").build();
        roleRepository.save(role_user);

        // when
        List<Role> roles = roleRepository.findAll();

        // then
        assertThat(roles).isNotNull();
    }

    @Test
    public void getRoleByIdTest() {
        // given

        // when
        Optional<Role> savedRole = roleRepository.findById(role.getId());

        // then
        assertThat(savedRole).isNotNull();
    }

    @Test
    public void updateRoleTest() {
        // given

        // when
        Role savedRole = roleRepository.findById(role.getId()).get();
        savedRole.setName("ROLE1");
        Role updatedRole = roleRepository.save(savedRole);

        // then
        assertThat(updatedRole).isNotNull();
        assertThat(updatedRole.getName()).isEqualTo("ROLE1");
    }

    @Test
    public void deleteRoleTest() {
        // given

        // when
        roleRepository.delete(role);
        Optional<Role> optionalRole = roleRepository.findById(role.getId());

        // then
        assertThat(optionalRole).isEmpty();
    }

    @Test
    public void findRoleByNameSuccessfullyTest() {
        // given

        // when
        Optional<Role> foundRole = roleRepository.findByName("ROLE_MANAGER");

        // then
        assertThat(foundRole).isNotNull();
    }

    @Test
    public void findRoleByNameUnsuccessfullyTest() {
        // given

        // when
        Optional<Role> foundRole = roleRepository.findByName("ROLE");

        // then
        assertThat(foundRole).isEmpty();
    }
}
