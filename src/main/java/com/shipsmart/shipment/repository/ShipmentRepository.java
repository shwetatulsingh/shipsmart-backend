package com.shipsmart.shipment.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shipsmart.shipment.entity.Shipment;
import com.shipsmart.shipment.enums.ShipmentStatus;

/**
 * Spring Data generates the implementation at startup by parsing the method
 * names, e.g. {@code findByTrackingNumber} becomes
 * {@code ... WHERE tracking_number = ?}.
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    boolean existsByTrackingNumber(String trackingNumber);

    /** Uses index idx_shipments_status_created_at when sorted by createdAt. */
    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);
}
