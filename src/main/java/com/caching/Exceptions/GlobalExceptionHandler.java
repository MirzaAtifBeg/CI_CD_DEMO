package com.caching.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for managing and responding to exceptions in the application.
 *
 * <p>This class is responsible for handling specific exceptions thrown by the application,
 * providing appropriate HTTP responses with error messages and status codes. It uses
 * {@link RestControllerAdvice} to intercept exceptions globally.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link GeoServiceException} by returning a {@link ResponseEntity} with the exception message
     * and a HTTP status of {@link HttpStatus#BAD_REQUEST}.
     *
     * @param ex the {@link GeoServiceException} to handle.
     * @return a {@link ResponseEntity} containing the error message and a BAD_REQUEST status.
     */
    @ExceptionHandler(GeoServiceException.class)
    public ResponseEntity<String> handleGeoServiceException(GeoServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general exceptions by returning a {@link ResponseEntity} with a generic error message
     * and a HTTP status of {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     *
     * @param ex the general {@link Exception} to handle.
     * @return a {@link ResponseEntity} containing a generic error message and an INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}