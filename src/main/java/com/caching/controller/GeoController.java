package com.caching.controller;

import com.caching.exceptions.GeoServiceException;
import com.caching.service.GeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for geocoding services, providing endpoints for both forward and reverse geocoding.
 *
 * <p>This controller defines two endpoints:</p>
 * <ul>
 *   <li>"/forward-geocoding" for converting an address to latitude and longitude.</li>
 *   <li>"/reverse-geocoding" for converting latitude and longitude to an address.</li>
 * </ul>
 */

@RestController
@Slf4j
public class GeoController {

    @Autowired
    private GeoService geoService;


//    GeoController(GeoService geoService){
//        this.geoService=geoService;
//    }

    /**
     * Endpoint to perform forward geocoding by converting an address to latitude and longitude.
     *
     * @param address the address to be geocoded.
     * @return a {@link Map<String,Double>} containing the latitude and longitude.
     */
    @GetMapping("/geocoding")
    public Map<String,Double> forwardGeoCoding(@RequestParam String address) {
        Map<String,Double> latLong = geoService.getCordinates(address);
        return latLong;
    }

    /**
     * Endpoint to perform reverse geocoding by converting latitude and longitude to an address.
     *
     * @param latitude the latitude to be reverse geocoded.
     * @param longitude the longitude to be reverse geocoded.
     * @return a {@link ResponseEntity} containing the address as a string.
     */

    @GetMapping("/reverse-geocoding")
    public ResponseEntity<String> reverseGeocoding(@RequestParam double latitude, @RequestParam double longitude) {
        String address = geoService.getAddress(latitude, longitude);
        return ResponseEntity.ok(address);
    }
}

