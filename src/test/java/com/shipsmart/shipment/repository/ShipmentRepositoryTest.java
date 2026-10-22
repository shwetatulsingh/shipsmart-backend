package com.shipsmart.shipment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.shipsmart.shipment.config.JpaConfig;
import com.shipsmart.shipment.config.TimeConfig;
import com.shipsmart.shipment.entity.Shipment;
import com.shipsmart.shipment.entity.ShipmentStatusHistory;
import com.shipsmart.shipment.enums.ShipmentStatus;
import com.shipsmart.shipment.support.TestShipments;

/**
 * Runs the Flyway migrations on H2 and exercises the repositories.
 * Each test runs in a transaction that is rolled back afterwards.
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use application-test.yml, not a generated H2
@Import({JpaConfig.class, TimeConfig.class})
class ShipmentRepositoryTest {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentStatusHistoryRepository historyRepository;

    @Test
    void savesShipmentAndFindsItByTrackingNumber() {
        Shipment saved = shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000001"));

        Shipment found = shipmentRepository.findByTrackingNumber("SS0000000001").orElseThrow();

        assertThat(found.getId()).isEqualTo(saved.getId()).isNotNull();
        assertThat(found.getStatus()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(found.getPickupAddress()).isEqualTo(TestShipments.pickupAddress());
        assertThat(found.getDeliveryAddress().getLine2()).isNull();
        assertThat(found.getPackageWeightKg()).isEqualByComparingTo("2.450");
        assertThat(shipmentRepository.existsByTrackingNumber("SS0000000001")).isTrue();
        assertThat(shipmentRepository.findByTrackingNumber("SS-UNKNOWN")).isEmpty();
    }

    @Test
    void fillsAuditTimestampsAndVersionOnInsert() {
        Shipment saved = shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000002"));

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getVersion()).isZero();
    }

    @Test
    void incrementsVersionOnUpdate() {
        Shipment saved = shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000003"));

        saved.changeStatus(ShipmentStatus.ASSIGNED);
        Shipment updated = shipmentRepository.saveAndFlush(saved);

        assertThat(updated.getStatus()).isEqualTo(ShipmentStatus.ASSIGNED);
        assertThat(updated.getVersion()).isEqualTo(1L);
    }

    @Test
    void rejectsDuplicateTrackingNumber() {
        shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000004"));

        assertThatThrownBy(() -> shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000004")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void filtersByStatusWithPagination() {
        for (int i = 1; i <= 3; i++) {
            shipmentRepository.save(TestShipments.shipment("SS000000010" + i));
        }
        Shipment delivered = TestShipments.shipment("SS0000000200");
        delivered.changeStatus(ShipmentStatus.DELIVERED);
        shipmentRepository.saveAndFlush(delivered);

        Page<Shipment> firstPage = shipmentRepository.findByStatus(
                ShipmentStatus.CREATED, PageRequest.of(0, 2, Sort.by("createdAt").descending()));

        assertThat(firstPage.getTotalElements()).isEqualTo(3);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.getContent()).hasSize(2)
                .allMatch(s -> s.getStatus() == ShipmentStatus.CREATED);
    }

    @Test
    void returnsStatusHistoryOldestFirst() {
        Shipment shipment = shipmentRepository.saveAndFlush(TestShipments.shipment("SS0000000300"));
        historyRepository.save(new ShipmentStatusHistory(shipment, null, ShipmentStatus.CREATED, "Shipment created"));
        historyRepository.save(new ShipmentStatusHistory(shipment, ShipmentStatus.CREATED, ShipmentStatus.ASSIGNED, "Courier assigned"));
        historyRepository.saveAndFlush(new ShipmentStatusHistory(shipment, ShipmentStatus.ASSIGNED, ShipmentStatus.PICKED_UP, null));

        List<ShipmentStatusHistory> history = historyRepository.findByShipmentIdOrderByChangedAtAscIdAsc(shipment.getId());

        assertThat(history).extracting(ShipmentStatusHistory::getNewStatus)
                .containsExactly(ShipmentStatus.CREATED, ShipmentStatus.ASSIGNED, ShipmentStatus.PICKED_UP);
        assertThat(history.get(0).getPreviousStatus()).isNull();
        assertThat(history).allSatisfy(h -> assertThat(h.getChangedAt()).isNotNull());
    }
}
