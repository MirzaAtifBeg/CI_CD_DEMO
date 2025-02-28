package com.caching.caching;


import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CacheConfig {
    private final int CACHE_MAX_SIZE=100;

    private int CACHE_TTL=7;

    /**
     * Configures and provides a {@link CacheManager} bean for managing application-level caching.
     *
     * <p>This method creates a {@link CaffeineCacheManager} with specified cache names
     * ("geocoding" and "reverse-geocoding"). The cache is configured with a maximum size
     * and a time-to-live (TTL) eviction policy. Evictions due to size or time are logged for monitoring purposes.</p>
     *
     * @return a configured {@link CacheManager} instance using Caffeine.
     */

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("geocoding", "reverse-geocoding");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(CACHE_MAX_SIZE)  // LRU eviction policy: cache size limit
                .expireAfterWrite(CACHE_TTL, TimeUnit.MINUTES)  // TTL eviction policy: time-based expiration
                .removalListener((key, value, cause) -> {
                    log.info("Cache entry evicted: Key = {}, Cause = {}", key, cause);
                })
        );

        log.info("CacheManager bean created and configured.");
        return cacheManager;
    }
}