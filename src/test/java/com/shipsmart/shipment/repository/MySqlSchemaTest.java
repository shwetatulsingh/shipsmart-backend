package com.shipsmart.shipment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.shipsmart.shipment.config.JpaConfig;
import com.shipsmart.shipment.config.TimeConfig;
import com.shipsmart.shipment.entity.Shipment;
import com.shipsmart.shipment.support.TestShipments;

/**
 * Runs the migrations against a real MySQL 8.4 in a throwaway Docker container,
 * catching anything H2's MySQL mode would let through.
 *
 * <p>Skipped (not failed) when Docker isn't running, so {@code mvn test}
 * works on any machine.
 */
@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TimeConfig.class})
class MySqlSchemaTest {

    // @ServiceConnection points the DataSource at this container automatically
    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void migrationsApplyAndEntitiesMatchTheMySqlSchema() {
        // Reaching this line means Flyway ran V1 and V2 and Hibernate's
        // ddl-auto=validate accepted the schema.
        Shipment saved = shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000009001"));

        assertThat(shipmentRepository.findByTrackingNumber("SS0000009001"))
                .get()
                .extracting(Shipment::getId)
                .isEqualTo(saved.getId());
    }
}
