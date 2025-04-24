package com.habitude.controllers;

import com.habitude.model.Subject;
import com.habitude.model.User;
import com.habitude.repository.SubjectRepository;
import com.habitude.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubjectController {

    private final SubjectRepository subjectRepo;
    private final UserRepository   userRepo;

    public SubjectController(SubjectRepository subjectRepo,
                             UserRepository userRepo) {
        this.subjectRepo = subjectRepo;
        this.userRepo    = userRepo;
    }

    // 1) Create a new subject for a user
    @PostMapping("/users/{userId}/subjects")
    @ResponseStatus(HttpStatus.CREATED)
    public Subject createSubject(@PathVariable Long userId,
                                 @RequestBody Subject input) {
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        input.setUser(owner);
        return subjectRepo.save(input);
    }

    // 2) List all subjects for a user
    @GetMapping("/users/{userId}/subjects")
    public List<Subject> getSubjectsForUser(@PathVariable Long userId) {
        return subjectRepo.findByUserId(userId);
    }

    // 3) Get a single subject by its ID
    @GetMapping("/subjects/{id}")
    public Subject getSubject(@PathVariable Long id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    // 4) Update a subject
    @PutMapping("/subjects/{id}")
    public Subject updateSubject(@PathVariable Long id,
                                 @RequestBody Subject updated) {
        Subject existing = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        existing.setName(updated.getName());
        existing.setDob(updated.getDob());
        existing.setNotes(updated.getNotes());
        return subjectRepo.save(existing);
    }

    // 5) Delete a subject
    @DeleteMapping("/subjects/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectRepo.deleteById(id);
    }
}
