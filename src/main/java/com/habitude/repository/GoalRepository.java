package com.habitude.repository;

import com.habitude.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findBySubjectId(Long subjectId);
}
