package com.shipsmart.shipment.config;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Turns on Spring Data auditing, which fills {@code @CreatedDate} and
 * {@code @LastModifiedDate} fields automatically on insert and update.
 *
 * <p>Timestamps come from the application {@link Clock} (UTC, see
 * {@link TimeConfig}) rather than the system clock, so tests can control them.
 *
 * <p>This lives in its own class, not on {@code ShipSmartApplication}, so
 * web-only test slices ({@code @WebMvcTest}, Phase 4) don't try to start JPA.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider(Clock clock) {
        return () -> Optional.of(Instant.now(clock));
    }
}
