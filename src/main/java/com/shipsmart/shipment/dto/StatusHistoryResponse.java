package com.shipsmart.shipment.dto;

import java.time.Instant;

import com.shipsmart.shipment.enums.ShipmentStatus;

/** One entry of {@code GET /api/v1/shipments/{id}/history}. */
public record StatusHistoryResponse(
        Long id,
        ShipmentStatus previousStatus,
        ShipmentStatus newStatus,
        Instant changedAt,
        String remarks) {
}
