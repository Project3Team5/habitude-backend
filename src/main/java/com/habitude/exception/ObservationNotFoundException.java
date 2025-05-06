package com.habitude.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObservationNotFoundException extends RuntimeException {
    public ObservationNotFoundException(Long id) {
        super("Observation not found with id " + id);
    }
}
