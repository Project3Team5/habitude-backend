//package com.habitude.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.habitude.model.Observation;
//import com.habitude.model.Subject;
//import com.habitude.model.User;
//import com.habitude.repository.ObservationRepository;
//import com.habitude.repository.SubjectRepository;
//import com.habitude.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class ObservationControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//    @Autowired private UserRepository userRepo;
//    @Autowired private SubjectRepository subjectRepo;
//    @Autowired private ObservationRepository obsRepo;
//
//    private User testUser;
//    private Subject testSubject;
//
//    @BeforeEach
//    void setup() {
//        obsRepo.deleteAll();
//        subjectRepo.deleteAll();
//        userRepo.deleteAll();
//
//        testUser = new User();
//        testUser.setName("Tester");
//        testUser.setEmail("tester@example.com");
//        testUser = userRepo.save(testUser);
//
//        testSubject = new Subject();
//        testSubject.setName("SubjectA");
//        testSubject.setDob(LocalDateTime.now().minusYears(6));
//        testSubject.setUser(testUser);
//        testSubject = subjectRepo.save(testSubject);
//    }
//
//    @Test
//    void testCreateObservation() throws Exception {
//        Observation obs = new Observation();
//        obs.setSubject(testSubject);
//        obs.setObserver(testUser);
//        obs.setBehavior("TestBehavior");
//        obs.setContext("Testing context");
//        obs.setTimestamp(LocalDateTime.now());
//
//        mockMvc.perform(post("/api/observations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(obs)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.behavior", is("TestBehavior")));
//    }
//
//    @Test
//    void testGetAllObservations() throws Exception {
//        // seed one record
//        Observation obs = new Observation();
//        obs.setSubject(testSubject);
//        obs.setObserver(testUser);
//        obs.setBehavior("Behavior1");
//        obs.setTimestamp(LocalDateTime.now());
//        obsRepo.save(obs);
//
//        mockMvc.perform(get("/api/observations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//
//    @Test
//    void testGetSummary() throws Exception {
//        mockMvc.perform(get("/api/observations/summary"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*").isNotEmpty());
//    }
//
//    @Test
//    void testGetTrendData() throws Exception {
//        mockMvc.perform(get("/api/observations/trend"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*").isNotEmpty());
//    }
//
//    @Test
//    void testGetTrendBySubject() throws Exception {
//        mockMvc.perform(get("/api/observations/trend/by-subject/{subjectId}", testSubject.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*").isNotEmpty());
//    }
//
//    @Test
//    void testMockData() throws Exception {
//        mockMvc.perform(get("/api/observations/mock"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
//    }
//
//    @Test
//    void testDeleteObservation() throws Exception {
//        Observation obs = new Observation();
//        obs.setSubject(testSubject);
//        obs.setObserver(testUser);
//        obs.setBehavior("ToBeDeleted");
//        obs.setTimestamp(LocalDateTime.now());
//        obs = obsRepo.save(obs);
//
//        // delete
//        mockMvc.perform(delete("/api/observations/{id}", obs.getId()))
//                .andExpect(status().isNoContent());//package com.habitude.controllers;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.habitude.model.Observation;
////import com.habitude.model.Subject;
////import com.habitude.model.User;
////import com.habitude.repository.ObservationRepository;
////import com.habitude.repository.SubjectRepository;
////import com.habitude.repository.UserRepository;
////import jakarta.transaction.Transactional;
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.http.MediaType;
////import org.springframework.test.context.ActiveProfiles;
////import org.springframework.test.web.servlet.MockMvc;
////
////import java.time.LocalDateTime;
////
////import static org.hamcrest.Matchers.*;
////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
////import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
////
////@SpringBootTest
////@AutoConfigureMockMvc
////@ActiveProfiles("test")
////@Transactional
////public class ObservationControllerTest {
////
////    @Autowired private MockMvc mockMvc;
////    @Autowired private ObjectMapper objectMapper;
////    @Autowired private UserRepository userRepo;
////    @Autowired private SubjectRepository subjectRepo;
////    @Autowired private ObservationRepository obsRepo;
////
////    private User testUser;
////    private Subject testSubject;
////
////    @BeforeEach
////    void setup() {
////        obsRepo.deleteAll();
////        subjectRepo.deleteAll();
////        userRepo.deleteAll();
////
////        testUser = new User();
////        testUser.setName("Tester");
////        testUser.setEmail("tester@example.com");
////        testUser = userRepo.save(testUser);
////
////        testSubject = new Subject();
////        testSubject.setName("SubjectA");
////        testSubject.setDob(LocalDateTime.now().minusYears(6));
////        testSubject.setUser(testUser);
////        testSubject = subjectRepo.save(testSubject);
////    }
////
////    @Test
////    void testCreateObservation() throws Exception {
////        Observation obs = new Observation();
////        obs.setSubject(testSubject);
////        obs.setObserver(testUser);
////        obs.setBehavior("TestBehavior");
////        obs.setContext("Testing context");
////        obs.setTimestamp(LocalDateTime.now());
////
////        mockMvc.perform(post("/api/observations")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(obs)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.id").exists())
////                .andExpect(jsonPath("$.behavior", is("TestBehavior")));
////    }
////
////    @Test
////    void testGetAllObservations() throws Exception {
////        // seed one record
////        Observation obs = new Observation();
////        obs.setSubject(testSubject);
////        obs.setObserver(testUser);
////        obs.setBehavior("Behavior1");
////        obs.setTimestamp(LocalDateTime.now());
////        obsRepo.save(obs);
////
////        mockMvc.perform(get("/api/observations"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$", hasSize(1)));
////    }
////
////    @Test
////    void testGetSummary() throws Exception {
////        mockMvc.perform(get("/api/observations/summary"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.*").isNotEmpty());
////    }
////
////    @Test
////    void testGetTrendData() throws Exception {
////        mockMvc.perform(get("/api/observations/trend"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.*").isNotEmpty());
////    }
////
////    @Test
////    void testGetTrendBySubject() throws Exception {
////        mockMvc.perform(get("/api/observations/trend/by-subject/{subjectId}", testSubject.getId()))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.*").isNotEmpty());
////    }
////
////    @Test
////    void testMockData() throws Exception {
////        mockMvc.perform(get("/api/observations/mock"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
////    }
////
////    @Test
////    void testDeleteObservation() throws Exception {
////        Observation obs = new Observation();
////        obs.setSubject(testSubject);
////        obs.setObserver(testUser);
////        obs.setBehavior("ToBeDeleted");
////        obs.setTimestamp(LocalDateTime.now());
////        obs = obsRepo.save(obs);
////
////        // delete
////        mockMvc.perform(delete("/api/observations/{id}", obs.getId()))
////                .andExpect(status().isNoContent());
////
////        // GET afterwards currently returns 5xx
////        mockMvc.perform(get("/api/observations/{id}", obs.getId()))
////                .andExpect(status().isNotFound());
////    }
////}
//
//        // GET afterwards currently returns 5xx
//        mockMvc.perform(get("/api/observations/{id}", obs.getId()))
//                .andExpect(status().isNotFound());
//    }
//}
