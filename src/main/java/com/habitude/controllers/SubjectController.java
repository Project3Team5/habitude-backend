package com.habitude.controllers;

import com.habitude.dto.SubjectCreateDto;
import com.habitude.dto.SubjectUpdateDto;
import com.habitude.dto.SubjectResponseDto;
import com.habitude.model.Subject;
import com.habitude.model.User;
import com.habitude.repository.SubjectRepository;
import com.habitude.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SubjectController {

    private final SubjectRepository subjectRepo;
    private final UserRepository userRepo;

    public SubjectController(SubjectRepository subjectRepo, UserRepository userRepo) {
        this.subjectRepo = subjectRepo;
        this.userRepo = userRepo;
    }

    // Create subject for a user
    @PostMapping("/users/{userId}/subjects")
    public ResponseEntity<SubjectResponseDto> createSubject(@PathVariable Long userId,
                                                            @Valid @RequestBody SubjectCreateDto dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Subject subject = new Subject(user, dto.getName(), dto.getDob(), dto.getNotes());
        Subject saved = subjectRepo.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SubjectResponseDto(saved));
    }

    // List all subjects for a user
    @GetMapping("/users/{userId}/subjects")
    public ResponseEntity<List<SubjectResponseDto>> getSubjectsForUser(@PathVariable Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<SubjectResponseDto> subjects = subjectRepo.findByUserId(userId).stream()
                .map(SubjectResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(subjects);
    }

    // Get a single subject
    @GetMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponseDto> getSubject(@PathVariable Long id) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        return ResponseEntity.ok(new SubjectResponseDto(subject));
    }

    // Update a subject
    @PutMapping("/subjects/{id}")
    public ResponseEntity<SubjectResponseDto> updateSubject(@PathVariable Long id,
                                                            @Valid @RequestBody SubjectUpdateDto dto) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        if (dto.getName() != null) subject.setName(dto.getName());
        if (dto.getDob() != null) subject.setDob(dto.getDob());
        if (dto.getNotes() != null) subject.setNotes(dto.getNotes());

        Subject updated = subjectRepo.save(subject);
        return ResponseEntity.ok(new SubjectResponseDto(updated));
    }

    // Delete a subject
    @DeleteMapping("/subjects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable Long id) {
        if (!subjectRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }
        subjectRepo.deleteById(id);
    }
}