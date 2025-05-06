package com.habitude.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SubjectControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private GoalRepository goalRepo;        // ← added

    private User testUser;

    @BeforeEach
    void setup() {
        // delete children before parents
        goalRepo.deleteAll();                          // ← clear goals first
        subjectRepo.deleteAll();
        userRepo.deleteAll();

        // now seed a fresh user
        testUser = new User();
        testUser.setName("Test Parent");
        testUser.setEmail("parent@example.com");
        testUser = userRepo.save(testUser);
    }

    @Test
    void testCreateSubject() throws Exception {
        Subject subject = new Subject();
        subject.setName("Alice");
        subject.setDob(LocalDateTime.now().minusYears(6));
        subject.setNotes("No notes");

        mockMvc.perform(post("/api/users/{userId}/subjects", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.notes", is("No notes")));
    }

    @Test
    void testGetSubjectsForUser() throws Exception {
        Subject s1 = new Subject();
        s1.setName("Child1");
        s1.setDob(LocalDateTime.now().minusYears(7));
        s1.setUser(testUser);
        subjectRepo.save(s1);

        Subject s2 = new Subject();
        s2.setName("Child2");
        s2.setDob(LocalDateTime.now().minusYears(5));
        s2.setUser(testUser);
        subjectRepo.save(s2);

        mockMvc.perform(get("/api/users/{userId}/subjects", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name=='Child1')]").exists())
                .andExpect(jsonPath("$[?(@.name=='Child2')]").exists());
    }

    @Test
    void testGetSubjectById() throws Exception {
        Subject s = new Subject();
        s.setName("Bob");
        s.setDob(LocalDateTime.now().minusYears(8));
        s.setUser(testUser);
        Subject saved = subjectRepo.save(s);

        mockMvc.perform(get("/api/subjects/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bob")));
    }

    @Test
    void testUpdateSubject() throws Exception {
        Subject s = new Subject();
        s.setName("Carol");
        s.setDob(LocalDateTime.now().minusYears(9));
        s.setNotes("Initial notes");
        s.setUser(testUser);
        Subject saved = subjectRepo.save(s);

        Subject updated = new Subject();
        updated.setName("Carol Updated");
        updated.setDob(saved.getDob().plusYears(1));
        updated.setNotes("Updated notes");

        mockMvc.perform(put("/api/subjects/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Carol Updated")))
                .andExpect(jsonPath("$.notes", is("Updated notes")));
    }

    @Test
    void testDeleteSubject() throws Exception {
        Subject s = new Subject();
        s.setName("Dave");
        s.setDob(LocalDateTime.now().minusYears(4));
        s.setUser(testUser);
        Subject saved = subjectRepo.save(s);

        mockMvc.perform(delete("/api/subjects/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/subjects/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
