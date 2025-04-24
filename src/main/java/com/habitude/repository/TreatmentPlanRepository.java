package com.habitude.repository;

import com.habitude.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    List<TreatmentPlan> findBySubjectId(Long subjectId);
}
