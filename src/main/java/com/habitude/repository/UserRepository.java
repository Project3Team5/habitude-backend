package com.habitude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.habitude.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
