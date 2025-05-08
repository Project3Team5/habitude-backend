package com.habitude.controllers;

import com.habitude.dto.UserLoginDto;
import com.habitude.dto.UserResponseDto;
import com.habitude.dto.UserSignupDto;
import com.habitude.dto.UserUpdateDto;
import com.habitude.model.User;
import com.habitude.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> all() {
        return repo.findAll();
    }

    // BCrypt for hashing password
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody UserSignupDto dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        repo.save(user);

        return ResponseEntity.ok(new UserResponseDto(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto dto, HttpServletRequest request) {
        Optional<User> userOpt = repo.findByEmail(dto.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                request.getSession(true);
                return ResponseEntity.ok(new UserResponseDto(user));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> get(@PathVariable Long id) {
        User user = repo.findById(id).orElseThrow();
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        User user = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        if (dto.getNewPassword() != null) {
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        User updated = repo.save(user);
        return ResponseEntity.ok(new UserResponseDto(updated));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
