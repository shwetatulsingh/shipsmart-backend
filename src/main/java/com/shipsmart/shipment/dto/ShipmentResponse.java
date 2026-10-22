package com.shipsmart.shipment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.shipsmart.shipment.enums.ShipmentStatus;

/** A shipment as returned by the API. Timestamps are UTC. */
public record ShipmentResponse(
        Long id,
        String trackingNumber,
        String customerName,
        String customerEmail,
        AddressDto pickupAddress,
        AddressDto deliveryAddress,
        String packageDescription,
        BigDecimal packageWeightKg,
        ShipmentStatus status,
        LocalDate estimatedDeliveryDate,
        Instant createdAt,
        Instant updatedAt) {
}
