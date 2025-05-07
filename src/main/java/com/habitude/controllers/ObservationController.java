package com.habitude.controllers;

import com.habitude.model.Observation;
import com.habitude.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    // summary for mock data
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return observationService.getSummary();
    }

    // list all

    @GetMapping("/subjects/{subjectId}")
    public List<Observation> getAllObservationsBySubject(@PathVariable Long subjectId) {
        return observationService.getAllObservationsBySubject(subjectId);
    }



    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<Observation> getObservation(@PathVariable Long id) {
        Observation obs = observationService.getObservationById(id);
        return ResponseEntity.ok(obs);
    }

    // trend all
    @GetMapping("/trend")
    public Map<String, Object> getTrendData() {
        return observationService.getTrendData();
    }

    // trend by subject
    @GetMapping("/trend/by-subject/{subjectId}")
    public Map<String, Object> getTrendDataBySubject(@PathVariable Long subjectId) {
        return observationService.getTrendDataBySubject(subjectId);
    }

    // mock
    @GetMapping("/mock")
    public List<Observation> mockData() {
        return observationService.getMockObservations();
    }

    // create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Observation createObservation(@RequestBody Observation observation) {
        return observationService.saveObservation(observation);
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<Observation> updateObservation(@PathVariable Long id,
                                                         @RequestBody Observation updated) {
        Observation saved = observationService.updateObservation(id, updated);
        return ResponseEntity.ok(saved);
    }

    // delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
    }
}
