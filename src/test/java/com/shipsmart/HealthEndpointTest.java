package com.shipsmart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Starts the full application on a random port and calls it over real HTTP.
 * Needs no database, Kafka or Docker.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthEndpointReportsUp() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void livenessAndReadinessProbesAreAvailable() {
        assertThat(restTemplate.getForEntity("/actuator/health/liveness", String.class).getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(restTemplate.getForEntity("/actuator/health/readiness", String.class).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void healthDoesNotExposeComponentDetails() {
        String body = restTemplate.getForObject("/actuator/health", String.class);

        assertThat(body).doesNotContain("diskSpace");
    }

    @Test
    void sensitiveActuatorEndpointsAreNotExposed() {
        assertThat(restTemplate.getForEntity("/actuator/env", String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForEntity("/actuator/beans", String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
