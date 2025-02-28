package com.caching.exceptions;


/**
 * Custom exception for errors related to the geocoding service.
 *
 * <p>This exception is thrown when the {@link com.caching.service.GeoService} encounters an error
 * during geocoding operations, such as invalid input, service unavailability, or processing failures.</p>
 *
 * <p>It extends {@link RuntimeException}, allowing it to be thrown without the need to declare it in
 * method signatures, and can be used to provide a more specific exception for geocoding-related issues.</p>
 */
public class GeoServiceException extends RuntimeException {
    /**
     * Constructs a new {@link GeoServiceException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public GeoServiceException(String message) {
        super(message);
    }
}


