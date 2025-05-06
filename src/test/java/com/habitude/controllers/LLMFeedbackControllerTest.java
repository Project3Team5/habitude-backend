package com.habitude.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitude.model.LLMFeedback;
import com.habitude.model.Observation;
import com.habitude.model.Subject;
import com.habitude.model.User;
import com.habitude.repository.LLMFeedbackRepository;
import com.habitude.repository.ObservationRepository;
import com.habitude.repository.SubjectRepository;
import com.habitude.repository.UserRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LLMFeedbackControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private ObservationRepository obsRepo;
    @Autowired private LLMFeedbackRepository fbRepo;

    private User testUser;
    private Subject testSubject;
    private Observation testObs;

    @BeforeEach
    void setup() {
        fbRepo.deleteAll();
        obsRepo.deleteAll();
        subjectRepo.deleteAll();
        userRepo.deleteAll();

        // create user
        testUser = new User();
        testUser.setName("FBTester");
        testUser.setEmail("fbtester@example.com");
        testUser = userRepo.save(testUser);

        // create subject
        testSubject = new Subject();
        testSubject.setName("SubjectFB");
        testSubject.setDob(LocalDateTime.now().minusYears(6));
        testSubject.setUser(testUser);
        testSubject = subjectRepo.save(testSubject);

        // create observation
        testObs = new Observation();
        testObs.setSubject(testSubject);
        testObs.setObserver(testUser);
        testObs.setBehavior("BehaveFB");
        testObs.setTimestamp(LocalDateTime.now());
        testObs = obsRepo.save(testObs);
    }

    @Test
    void testCreateFeedback() throws Exception {
        LLMFeedback fb = new LLMFeedback();
        fb.setSuggestion("AI Suggestion");
        fb.setAccepted(true);
        fb.setConfidenceScore(0.85f);

        mockMvc.perform(post("/api/observations/{obsId}/llm-feedback", testObs.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fb)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.suggestion", is("AI Suggestion")))
                .andExpect(jsonPath("$.accepted", is(true)))
                .andExpect(jsonPath("$.confidenceScore", is(0.85)));
    }

    @Test
    void testListForObservation() throws Exception {
        // seed feedback
        LLMFeedback fb1 = new LLMFeedback();
        fb1.setObservation(testObs);
        fb1.setSuggestion("S1");
        fb1.setAccepted(false);
        fb1.setConfidenceScore(0.5f);
        fbRepo.save(fb1);

        mockMvc.perform(get("/api/observations/{obsId}/llm-feedback", testObs.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].suggestion", is("S1")));
    }

    @Test
    void testGetAllFeedback() throws Exception {
        // seed two feedback entries
        for (int i = 1; i <= 2; i++) {
            LLMFeedback fb = new LLMFeedback();
            fb.setObservation(testObs);
            fb.setSuggestion("Bulk"+i);
            fb.setAccepted(i%2==0);
            fb.setConfidenceScore(0.6f + i*0.1f);
            fbRepo.save(fb);
        }

        mockMvc.perform(get("/api/llm-feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetOneUpdateAndDelete() throws Exception {
        // create one feedback
        LLMFeedback fb = new LLMFeedback();
        fb.setObservation(testObs);
        fb.setSuggestion("ToUpdate");
        fb.setAccepted(false);
        fb.setConfidenceScore(0.7f);
        fb = fbRepo.save(fb);

        // get by id
        mockMvc.perform(get("/api/llm-feedback/{id}", fb.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestion", is("ToUpdate")));

        // update
        fb.setSuggestion("Updated");
        fb.setAccepted(true);
        fb.setConfidenceScore(0.95f);
        mockMvc.perform(put("/api/llm-feedback/{id}", fb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fb)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestion", is("Updated")))
                .andExpect(jsonPath("$.accepted", is(true)))
                .andExpect(jsonPath("$.confidenceScore", is(0.95)));

        // delete
        mockMvc.perform(delete("/api/llm-feedback/{id}", fb.getId()))
                .andExpect(status().isNoContent());

        // ensure gone
        mockMvc.perform(get("/api/llm-feedback/{id}", fb.getId()))
                .andExpect(status().isNotFound());
    }
}
