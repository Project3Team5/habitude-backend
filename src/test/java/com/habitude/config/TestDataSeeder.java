package com.habitude.config;

import com.habitude.model.*;
import com.habitude.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile("itest")  // only active during `-Dspring.profiles.active=itest`
public class TestDataSeeder {

    @Bean
    public CommandLineRunner seedTestData(
            UserRepository userRepo,
            SubjectRepository subjectRepo,
            GoalRepository goalRepo,
            TreatmentPlanRepository treatmentPlanRepo,
            ObservationRepository observationRepo,
            LLMFeedbackRepository llmFeedbackRepo
    ) {
        return args -> {
            // skip if already seeded
            if (userRepo.count() > 0) {
                System.out.println("⚠ Test seed skipped: data already exists");
                return;
            }

            // 1) create a single user
            User user = new User();
            user.setEmail("testuser@example.com");
            user.setName("Test User");
            user.setRole("parent");
            userRepo.save(user);

            // 2) one subject
            Subject subject = new Subject();
            subject.setName("TestSubject");
            subject.setDob(LocalDateTime.now().minusYears(6));
            subject.setUser(user);
            subjectRepo.save(subject);

            // 3) one goal
            Goal goal = new Goal();
            goal.setSubject(subject);
            goal.setDescription("Test Goal for " + subject.getName());
            goal.setStatus("Active");
            goalRepo.save(goal);

            // 4) one treatment plan — **must** use setPlan() here
            TreatmentPlan treatment = new TreatmentPlan();
            treatment.setSubject(subject);
            treatment.setGoal(goal);
            treatment.setPlan("Test Treatment Plan for " + subject.getName());  // ← this populates the non‐null `plan` column
            treatmentPlanRepo.save(treatment);

            // 5) one observation
            Observation obs = new Observation();
            obs.setSubject(subject);
            obs.setObserver(user);
            obs.setBehavior("Test Behavior");
            obs.setContext("Testing context");
            obs.setTimestamp(LocalDateTime.now().minusDays(1));
            observationRepo.save(obs);

            // 6) one piece of LLM feedback
            LLMFeedback feedback = new LLMFeedback();
            feedback.setObservation(obs);
            feedback.setSuggestion("Test Suggestion");
            feedback.setAccepted(true);
            feedback.setConfidenceScore(0.85f);
            llmFeedbackRepo.save(feedback);

            System.out.println("✔ Test data seeded.");
        };
    }
}
