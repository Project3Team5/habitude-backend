package com.habitude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.habitude.model.LLMFeedback;
import java.util.List;

public interface LLMFeedbackRepository extends JpaRepository<LLMFeedback, Long> {
    List<LLMFeedback> findByObservationId(Long observationId);
}

