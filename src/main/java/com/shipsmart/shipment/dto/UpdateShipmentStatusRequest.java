package com.shipsmart.shipment.dto;

import com.shipsmart.shipment.enums.ShipmentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Body of {@code PATCH /api/v1/shipments/{id}/status} (endpoint added in Phase 3). */
public record UpdateShipmentStatusRequest(
        @NotNull ShipmentStatus status,
        @Size(max = 500) String remarks) {
}
