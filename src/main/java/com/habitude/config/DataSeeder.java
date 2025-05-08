package com.habitude.config;

import com.habitude.model.*;
import com.habitude.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.*;

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
            // if any demo user exists, skip seeding entirely to avoid duplicates
            if (userRepo.existsByEmail("mickey.mouse@habitude.com")) {
                System.out.println("⚠ Demo seed skipped: demo data already exists.");
                return;
            }

            // 1) create demo users with cartoon character names
            List<User> users = new ArrayList<>();
            User u1 = new User(); u1.setName("Mickey Mouse"); u1.setEmail("mickey.mouse@habitude.com");
            User u2 = new User(); u2.setName("Bugs Bunny"); u2.setEmail("bugs.bunny@habitude.com");
            User u3 = new User(); u3.setName("Scooby Doo"); u3.setEmail("scooby.doo@habitude.com");
            User u4 = new User(); u4.setName("SpongeBob SquarePants"); u4.setEmail("spongebob.squarepants@habitude.com");
            User u5 = new User(); u5.setName("Daffy Duck"); u5.setEmail("daffy.duck@habitude.com");
            users.addAll(Arrays.asList(u1, u2, u3, u4, u5));
            userRepo.saveAll(users);

            // 2) define 3 subjects per user
            Map<User, List<String>> subjectMap = new LinkedHashMap<>();
            subjectMap.put(u1, Arrays.asList("Bart Simpson", "Lisa Simpson", "Maggie Simpson"));
            subjectMap.put(u2, Arrays.asList("Tom Cat", "Jerry Mouse", "Spike Bulldog"));
            subjectMap.put(u3, Arrays.asList("Fred Flintstone", "Barney Rubble", "Pebbles Flintstone"));
            subjectMap.put(u4, Arrays.asList("Patrick Star", "Squidward Tentacles", "Sandy Cheeks"));
            subjectMap.put(u5, Arrays.asList("Peppa Pig", "George Pig", "Daddy Pig"));

            LocalDateTime now = LocalDateTime.now();
            Random rand = new Random();

            // master list of possible observations
            List<ObservationData> masterObs = Arrays.asList(
                    new ObservationData("Tantrum", "During transition to new activity", Observation.Intensity.HIGH),
                    new ObservationData("Elopement", "When asked to join circle time", Observation.Intensity.MEDIUM),
                    new ObservationData("Self-injury", "When denied preferred toy", Observation.Intensity.MEDIUM),
                    new ObservationData("Non-compliance", "During cleanup requests", Observation.Intensity.LOW),
                    new ObservationData("Aggression", "During peer play", Observation.Intensity.HIGH),
                    new ObservationData("Inattention", "During group instruction", Observation.Intensity.LOW),
                    new ObservationData("Hand flapping", "When excited", Observation.Intensity.LOW),
                    new ObservationData("Property destruction", "During frustration", Observation.Intensity.MEDIUM)
            );

            // 3) for each user, create subjects and random observations
            subjectMap.forEach((user, names) -> {
                for (String sName : names) {
                    Subject subject = new Subject();
                    subject.setName(sName);
                    subject.setDob(now.minusYears(4 + rand.nextInt(5))); // age between 4-8
                    subject.setUser(user);
                    subjectRepo.save(subject);

                    // pick 4 random distinct observations
                    Collections.shuffle(masterObs, rand);
                    List<ObservationData> picks = masterObs.subList(0, 4);

                    for (int i = 0; i < picks.size(); i++) {
                        ObservationData od = picks.get(i);
                        Observation obs = new Observation();
                        obs.setSubject(subject);
                        obs.setObserver(user);
                        obs.setBehavior(od.behavior);
                        obs.setContext(od.context);
                        obs.setIntensity(od.intensity);
                        obs.setTimestamp(now.minusDays(rand.nextInt(7) + 1));
                        observationRepo.save(obs);

                        // one treatment plan per observation
                        TreatmentPlan plan = new TreatmentPlan();
                        plan.setSubject(subject);
                        plan.setPlan(generatePlanForBehavior(od.behavior));
                        treatmentPlanRepo.save(plan);
                    }
                }
            });

            System.out.println("✅ Demo data seeded successfully.");
        };
    }

    // helper to generate a simple plan based on behavior
    private String generatePlanForBehavior(String behavior) {
        switch (behavior) {
            case "Tantrum": return "Use clear first/then language and offer choices.";
            case "Elopement": return "Implement visual boundaries and reinforce staying within area.";
            case "Self-injury": return "Provide alternative coping strategies and sensory tools.";
            case "Non-compliance": return "Use a token economy system for compliance.";
            case "Aggression": return "Teach and reinforce calm-down techniques.";
            case "Inattention": return "Incorporate brief movement breaks and visual cues.";
            case "Hand flapping": return "Offer sensory substitutes and structured outlets.";
            case "Property destruction": return "Build frustration tolerance with incremental tasks.";
            default: return "Apply positive reinforcement strategies.";
        }
    }

    // data holder for observations
    private static class ObservationData {
        String behavior;
        String context;
        Observation.Intensity intensity;
        ObservationData(String b, String c, Observation.Intensity i) {
            this.behavior = b;
            this.context = c;
            this.intensity = i;
        }
    }
}
