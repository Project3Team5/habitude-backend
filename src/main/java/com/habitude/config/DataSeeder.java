package com.habitude.config;

import com.habitude.model.*;
import com.habitude.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile({"dev", "prod"})
public class DataSeeder {

    @Bean
    CommandLineRunner seedDemoData(
            UserRepository userRepo,
            SubjectRepository subjectRepo,
            GoalRepository goalRepo,
            TreatmentPlanRepository treatmentPlanRepo,
            ObservationRepository observationRepo,
            LLMFeedbackRepository llmFeedbackRepo
    ) {
        return args -> {
            if (userRepo.count() > 0) {
                System.out.println("⚠ Demo seed skipped: users already exist.");
                return;
            }

            // 1) create demo user
            User user = new User();
            user.setEmail("demo_user@habitude.com");
            user.setName("Demo User");
            userRepo.save(user);

            // 2) create a subject
            Subject subject = new Subject();
            subject.setName("Demo Subject");
            subject.setDob(LocalDateTime.now().minusYears(5));
            subject.setUser(user);
            subjectRepo.save(subject);

            // 3) a goal
            Goal goal = new Goal();
            goal.setSubject(subject);
            goal.setDescription("Complete ABA Therapy Module");
            goal.setStatus("Active");
            goalRepo.save(goal);

            // 4) a treatment plan
            TreatmentPlan plan = new TreatmentPlan();
            plan.setSubject(subject);
            plan.setPlan("Reward-based treatment to reinforce positive behavior.");
            treatmentPlanRepo.save(plan);

            // 5) an observation
            Observation obs = new Observation();
            obs.setSubject(subject);
            obs.setObserver(user);  // <— MUST set the observer!
            obs.setBehavior("Refused task");
            obs.setContext("During homework");
            obs.setIntensity(Observation.Intensity.MEDIUM);
            obs.setTimestamp(LocalDateTime.now().minusDays(1));
            observationRepo.save(obs);

            // 6) LLM feedback on that observation
            LLMFeedback feedback = new LLMFeedback();
            feedback.setObservation(obs);
            feedback.setSuggestion("Try breaking tasks into smaller steps with rewards.");
            feedback.setConfidenceScore(0.92f);
            feedback.setAccepted(false);
            llmFeedbackRepo.save(feedback);

            System.out.println("✅ Demo data seeded successfully.");
        };
    }
}
