package com.habitude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.habitude.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
