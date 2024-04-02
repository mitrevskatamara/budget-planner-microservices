package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailOrUsername(String email, String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findAllByStatus(Boolean status);
}
