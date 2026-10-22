package com.shipsmart.shipment.enums;

/**
 * Lifecycle of a shipment.
 *
 * <p>Stored in the database by name (e.g. {@code "IN_TRANSIT"}), so constants
 * may be added freely but must never be renamed without a data migration.
 * The rules for which status may follow which are added in Phase 3.
 */
public enum ShipmentStatus {
    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    CANCELLED
}
