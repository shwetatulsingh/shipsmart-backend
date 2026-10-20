package com.shipsmart.shipment.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Time convention for the whole application: all timestamps are UTC.
 *
 * <p>Services will ask this {@link Clock} for "now" instead of calling
 * {@code Instant.now()} directly. In tests we can then swap in
 * {@code Clock.fixed(...)} and assert exact timestamps on shipments and
 * status-history rows.
 */
@Configuration(proxyBeanMethods = false)
public class TimeConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
