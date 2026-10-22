package com.shipsmart.shipment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shipsmart.shipment.entity.ShipmentStatusHistory;

public interface ShipmentStatusHistoryRepository extends JpaRepository<ShipmentStatusHistory, Long> {

    /**
     * Oldest change first. {@code id} breaks ties between changes recorded
     * in the same microsecond. Uses index idx_status_history_shipment_changed_at.
     */
    List<ShipmentStatusHistory> findByShipmentIdOrderByChangedAtAscIdAsc(Long shipmentId);
}
