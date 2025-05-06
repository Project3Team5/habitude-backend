package com.habitude.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitude.model.Goal;
import com.habitude.model.Subject;
import com.habitude.model.User;
import com.habitude.repository.GoalRepository;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private GoalRepository goalRepo;

    private Subject testSubject;

    @BeforeEach
    void setup() {
        goalRepo.deleteAll();
        subjectRepo.deleteAll();
        userRepo.deleteAll();

        // create a user and subject for goals
        User user = new User();
        user.setName("Test Parent");
        user.setEmail("parent@example.com");
        user = userRepo.save(user);

        testSubject = new Subject();
        testSubject.setName("Test Child");
        testSubject.setDob(LocalDate.now().minusYears(7).atStartOfDay());
        testSubject.setUser(user);
        testSubject = subjectRepo.save(testSubject);
    }

    @Test
    void testCreateGoal() throws Exception {
        Goal goal = new Goal();
        goal.setDescription("Learn to count");
        goal.setStatus("not started");
        // leave targetDate null

        mockMvc.perform(post("/api/subjects/{subjectId}/goals", testSubject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("Learn to count")))
                .andExpect(jsonPath("$.status", is("not started")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void testGetGoalsForSubject() throws Exception {
        // seed two goals
        Goal g1 = new Goal();
        g1.setSubject(testSubject);
        g1.setDescription("First goal");
        g1.setStatus("in progress");
        goalRepo.save(g1);

        Goal g2 = new Goal();
        g2.setSubject(testSubject);
        g2.setDescription("Second goal");
        g2.setStatus("not started");
        goalRepo.save(g2);

        mockMvc.perform(get("/api/subjects/{subjectId}/goals", testSubject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.description=='First goal')]").exists())
                .andExpect(jsonPath("$[?(@.description=='Second goal')]").exists());
    }

    @Test
    void testGetGoalById() throws Exception {
        Goal g = new Goal();
        g.setSubject(testSubject);
        g.setDescription("Solo goal");
        g.setStatus("achieved");
        Goal saved = goalRepo.save(g);

        mockMvc.perform(get("/api/goals/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Solo goal")))
                .andExpect(jsonPath("$.status", is("achieved")));
    }

    @Test
    void testUpdateGoal() throws Exception {
        // initial goal
        Goal g = new Goal();
        g.setSubject(testSubject);
        g.setDescription("Old desc");
        g.setStatus("not started");
        Goal saved = goalRepo.save(g);

        // prepare update
        LocalDate newDate = LocalDate.now().plusWeeks(2);
        Goal updated = new Goal();
        updated.setDescription("New desc");
        updated.setStatus("in progress");
        updated.setTargetDate(newDate);

        mockMvc.perform(put("/api/goals/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("New desc")))
                .andExpect(jsonPath("$.status", is("in progress")))
                .andExpect(jsonPath("$.targetDate", is(newDate.toString())));
    }

    @Test
    void testDeleteGoal() throws Exception {
        Goal g = new Goal();
        g.setSubject(testSubject);
        g.setDescription("To be deleted");
        g.setStatus("in progress");
        Goal saved = goalRepo.save(g);

        mockMvc.perform(delete("/api/goals/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // verify it's gone
        mockMvc.perform(get("/api/goals/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}

