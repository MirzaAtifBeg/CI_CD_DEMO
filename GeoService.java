package com.caching.service;

import com.caching.exceptions.GeoServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for geocoding and reverse geocoding operations.
 *
 * <p>This service interacts with the PositionStack API to fetch geocoding (address to coordinates)
 * and reverse geocoding (coordinates to address) data. It caches the results to optimize performance
 * and avoid repeated API calls for the same queries.</p>
 *
 * <p>The service also includes validation for input data (address, latitude, and longitude) and throws
 * a custom exception, {@link GeoServiceException}, in case of invalid or failed operations.</p>
 */
@Service
@Slf4j
public class GeoService {
    system.out.println("check");
    @Value("${positionstack.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    /**
     * Constructor for GeoService class. Injects the {@link RestTemplate} to make HTTP requests.
     *
     * @param restTemplate the {@link RestTemplate} instance used to make the HTTP calls.
     */

    public GeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the coordinates (latitude and longitude) for a given address using the PositionStack API.
     * Caches the result based on the address and skips caching for the address 'goa'.
     *
     * @param address the address for which the coordinates need to be fetched.
     * @return a Map<String,Double> containing the latitude and longitude in JSON format.
     * @throws GeoServiceException if the address is invalid, or if the coordinates cannot be fetched.
     */
    @Cacheable(cacheNames = "geocoding", key = "#address", unless = "#address.equalsIgnoreCase('goa')")
    public Map<String,Double> getCordinates(String address) {

        if (address == null || address.trim().isEmpty()) {
            throw new GeoServiceException("Invalid address provided.");
        }

        try {
            log.info("Fetching geocoding data for address: {}", address);
            String url = UriComponentsBuilder.fromHttpUrl("http://api.positionstack.com/v1/forward")
                    .queryParam("access_key", apiKey)
                    .queryParam("query", address)
                    .toUriString();
            Object apiResponse = restTemplate.getForObject(url, Object.class);
            if (apiResponse instanceof Map<?, ?> responseMap) {
                List<?> dataList = (List<?>) responseMap.get("data");
                Map<?,?> firstResult= (Map<?, ?>) dataList.get(0);
                Map<String,Double> response = new HashMap<>();
                response.put("latitude", (Double) firstResult.get("latitude"));
                response.put("longitude", (Double) firstResult.get("longitude"));
                log.info("Address '{}' maps to Lat/Long: {}", address, apiResponse);
                return response;
            }
            return new HashMap<>();
        } catch (Exception ex) {
            throw new GeoServiceException("Failed to fetch coordinates for the address: " + ex.getMessage());
        }
    }

    /**
     * Fetches the address for a given latitude and longitude using the PositionStack API.
     * Caches the result based on the latitude and longitude.
     *
     * @param latitude the latitude of the location to be reverse geocoded.
     * @param longitude the longitude of the location to be reverse geocoded.
     * @return a string containing the address corresponding to the given coordinates in JSON format.
     * @throws GeoServiceException if the latitude or longitude is out of range or if the address cannot be fetched.
     */
    @Cacheable(cacheNames = "reverse-geocoding", key = "{#latitude, #longitude}")
    public String getAddress(double latitude, double longitude) {

        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new GeoServiceException("Invalid latitude: Latitude must be between " + MIN_LATITUDE + " and " + MAX_LATITUDE + ".");
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new GeoServiceException("Invalid longitude: Longitude must be between " + MIN_LONGITUDE + " and " + MAX_LONGITUDE + ".");
        }

        try {
            log.info("Fetching reverse geocoding data for Lat/Long: {}, {}", latitude, longitude);
            String url = UriComponentsBuilder.fromHttpUrl("http://api.positionstack.com/v1/reverse")
                    .queryParam("access_key", apiKey)
                    .queryParam("query", latitude + "," + longitude)
                    .toUriString();

            Object apiResponse = restTemplate.getForObject(url, Object.class);
            if (apiResponse instanceof Map<?, ?> responseMap) {
                List<?> dataList = (List<?>) responseMap.get("data");
                Map<?, ?> firstResult = (Map<?, ?>) dataList.get(0);
                log.info("Lat/Long ({}, {}) maps to address: {}", latitude, longitude, apiResponse);
                return firstResult.get("label").toString();
            }
            return "";
        } catch (Exception ex) {
            throw new GeoServiceException("Failed to fetch address for the coordinates: " + ex.getMessage());
        }
    }
}

