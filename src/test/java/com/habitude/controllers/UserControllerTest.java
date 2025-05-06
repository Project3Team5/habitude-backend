package com.habitude.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitude.model.User;
import com.habitude.repository.LLMFeedbackRepository;
import com.habitude.repository.ObservationRepository;
import com.habitude.repository.TreatmentPlanRepository;
import com.habitude.repository.GoalRepository;
import com.habitude.repository.SubjectRepository;
import com.habitude.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    @Autowired private LLMFeedbackRepository feedbackRepo;
    @Autowired private ObservationRepository   obsRepo;
    @Autowired private TreatmentPlanRepository tpRepo;
    @Autowired private GoalRepository          goalRepo;
    @Autowired private SubjectRepository       subjectRepo;
    @Autowired private UserRepository          userRepo;
    @Autowired private MockMvc                 mockMvc;
    @Autowired private ObjectMapper            objectMapper;

    @BeforeEach
    public void setup() {
        // delete in reverse-FK order
        feedbackRepo.deleteAll();
        obsRepo.deleteAll();
        tpRepo.deleteAll();
        goalRepo.deleteAll();
        subjectRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setRole("parent");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.email", is("testuser@example.com")))
                .andExpect(jsonPath("$.role", is("parent")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setRole("therapist");

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setRole("parent");

        userRepo.save(user1);
        userRepo.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name == 'Alice')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Bob')]").exists());
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@example.com");
        user.setRole("parent");
        User savedUser = userRepo.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Charlie")))
                .andExpect(jsonPath("$.email", is("charlie@example.com")));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Step 1: Create a user
        String userJson = """
        {
          "email": "tobedeleted@example.com",
          "name": "To Be Deleted"
        }
        """;

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        // Step 2: Extract ID from response
        String response = result.getResponse().getContentAsString();
        Long userId = ((Integer) JsonPath.read(response, "$.id")).longValue();

        // Step 3: Delete the created user
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk());
    }
}
