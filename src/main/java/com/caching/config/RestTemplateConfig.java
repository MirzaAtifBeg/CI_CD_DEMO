package com.caching.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    /**
     * Configures and provides a {@link RestTemplate} bean for making HTTP requests.
     *
     * <p>This method creates a new {@link RestTemplate} instance, which can be used for
     * making HTTP requests in a Spring-based application. The {@link RestTemplate} bean
     * is registered in the Spring context for dependency injection.</p>
     *
     * @return a new instance of {@link RestTemplate}.
     */

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
