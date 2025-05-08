package com.habitude.repository;

import com.habitude.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findBySubjectId(Long subjectId);

}

