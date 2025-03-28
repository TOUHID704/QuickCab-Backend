package com.touhid.project.uber.uberApp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * RestClientConfig is the configuration class where we define a RestClient Bean.
 * The RestClient is used to make HTTP requests.
 */
@Configuration
public class RestClientConfig {

    /**
     * Creates and provides a RestClient Bean.
     * The RestClient is created using the static create() method.
     * This RestClient Bean will be injected into other parts of the application where it's needed.
     *
     * @return RestClient instance configured for making HTTP requests.
     */
    @Bean
    public RestClient restClient() {
        return RestClient.create();  // Creates a RestClient instance using its create() method.
    }
}
