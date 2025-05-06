package com.habitude.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitude.model.*;
import com.habitude.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TreatmentPlanControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private GoalRepository goalRepo;
    @Autowired private ObservationRepository obsRepo;
    @Autowired private TreatmentPlanRepository planRepo;

    private User testUser;
    private Subject testSubject;
    private Goal testGoal;
    private Observation testObservation;
    private TreatmentPlan existingPlan;

    @BeforeEach
    void setup() {
        planRepo.deleteAll();
        obsRepo.deleteAll();
        goalRepo.deleteAll();
        subjectRepo.deleteAll();
        userRepo.deleteAll();

        testUser = new User();
        testUser.setName("Tester");
        testUser.setEmail("tester@example.com");
        testUser = userRepo.save(testUser);

        testSubject = new Subject();
        testSubject.setName("SubjectA");
        testSubject.setDob(LocalDateTime.now().minusYears(5));
        testSubject.setUser(testUser);
        testSubject = subjectRepo.save(testSubject);

        testGoal = new Goal();
        testGoal.setSubject(testSubject);
        testGoal.setDescription("Test Goal");
        testGoal.setStatus("Active");
        testGoal = goalRepo.save(testGoal);

        testObservation = new Observation();
        testObservation.setSubject(testSubject);
        testObservation.setObserver(testUser);
        testObservation.setBehavior("Behavior1");
        testObservation.setTimestamp(LocalDateTime.now());
        testObservation = obsRepo.save(testObservation);

        existingPlan = new TreatmentPlan();
        existingPlan.setSubject(testSubject);
        existingPlan.setGoal(testGoal);
        existingPlan.setObservation(testObservation);
        existingPlan.setPlan("Initial plan details");
        existingPlan.setNextReview(LocalDateTime.now().plusDays(7));
        existingPlan.setNotes("Initial notes");
        existingPlan = planRepo.save(existingPlan);
    }

    @Test
    void testCreateTreatmentPlan() throws Exception {
        TreatmentPlan newPlan = new TreatmentPlan();
        newPlan.setPlan("New Plan");
        newPlan.setNextReview(LocalDateTime.now().plusDays(14));
        newPlan.setNotes("Some notes");
        newPlan.setGoal(testGoal);
        newPlan.setObservation(testObservation);

        mockMvc.perform(post("/api/subjects/{id}/treatment-plans", testSubject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlan)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.plan", is("New Plan")));
    }

    @Test
    void testListTreatmentPlansBySubject() throws Exception {
        mockMvc.perform(get("/api/subjects/{id}/treatment-plans", testSubject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(existingPlan.getId().intValue())));
    }

    @Test
    void testGetOneTreatmentPlan() throws Exception {
        mockMvc.perform(get("/api/treatment-plans/{id}", existingPlan.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(existingPlan.getId().intValue())));
    }

    @Test
    void testUpdateTreatmentPlan() throws Exception {
        existingPlan.setPlan("Updated Plan");
        existingPlan.setNotes("Updated notes");

        mockMvc.perform(put("/api/treatment-plans/{id}", existingPlan.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingPlan)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan", is("Updated Plan")))
                .andExpect(jsonPath("$.notes", is("Updated notes")));
    }

    @Test
    void testDeleteTreatmentPlan() throws Exception {
        mockMvc.perform(delete("/api/treatment-plans/{id}", existingPlan.getId()))
                .andExpect(status().isNoContent());
    }
}
