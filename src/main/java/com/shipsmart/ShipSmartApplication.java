package com.shipsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the ShipSmart backend.
 *
 * <p>This class lives in the root package {@code com.shipsmart} on purpose:
 * component scanning starts here, so every feature module placed underneath
 * it (today {@code com.shipsmart.shipment}, later e.g.
 * {@code com.shipsmart.notification}) is picked up automatically.
 */
@SpringBootApplication
public class ShipSmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipSmartApplication.class, args);
    }
}
