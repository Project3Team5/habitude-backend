package com.habitude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.habitude.model.Subject;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // find all subjects for a given user
    List<Subject> findByUserId(Long userId);
}
