package com.shipsmart.shipment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.shipsmart.shipment.dto.ShipmentResponse;
import com.shipsmart.shipment.dto.StatusHistoryResponse;
import com.shipsmart.shipment.entity.Shipment;
import com.shipsmart.shipment.entity.ShipmentStatusHistory;
import com.shipsmart.shipment.enums.ShipmentStatus;
import com.shipsmart.shipment.support.TestShipments;

/** Plain unit test: no Spring context, runs in milliseconds. */
class ShipmentMapperTest {

    private final ShipmentMapper mapper = new ShipmentMapper();

    @Test
    void mapsCreateRequestToNewEntityInCreatedStatus() {
        Shipment shipment = mapper.toNewEntity(TestShipments.createRequest(), "SS0000000001");

        assertThat(shipment.getTrackingNumber()).isEqualTo("SS0000000001");
        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(shipment.getId()).isNull();
        assertThat(shipment.getPickupAddress().getCity()).isEqualTo("Mumbai");
        assertThat(shipment.getDeliveryAddress().getPostalCode()).isEqualTo("560001");
        assertThat(shipment.getPackageWeightKg()).isEqualByComparingTo("2.450");
    }

    @Test
    void normalizesWhitespaceAndEmailCase() {
        Shipment shipment = mapper.toNewEntity(TestShipments.createRequest(), "SS0000000001");

        assertThat(shipment.getCustomerName()).isEqualTo("Priya Sharma");
        assertThat(shipment.getCustomerEmail()).isEqualTo("priya.sharma@example.com");
        assertThat(shipment.getDeliveryAddress().getLine2()).as("blank line2 stored as null").isNull();
    }

    @Test
    void mapsEntityToResponse() {
        Shipment shipment = TestShipments.shipment("SS0000000002");

        ShipmentResponse response = mapper.toResponse(shipment);

        assertThat(response.trackingNumber()).isEqualTo("SS0000000002");
        assertThat(response.customerEmail()).isEqualTo("priya.sharma@example.com");
        assertThat(response.status()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(response.pickupAddress().line1()).isEqualTo("Unit 4, Andheri Logistics Park");
        assertThat(response.deliveryAddress().countryCode()).isEqualTo("IN");
        assertThat(response.estimatedDeliveryDate()).isEqualTo(shipment.getEstimatedDeliveryDate());
    }

    @Test
    void mapsHistoryEntryToResponse() {
        ShipmentStatusHistory history = new ShipmentStatusHistory(
                TestShipments.shipment("SS0000000003"), ShipmentStatus.IN_TRANSIT, ShipmentStatus.DELAYED, "Weather delay");

        StatusHistoryResponse response = mapper.toResponse(history);

        assertThat(response.previousStatus()).isEqualTo(ShipmentStatus.IN_TRANSIT);
        assertThat(response.newStatus()).isEqualTo(ShipmentStatus.DELAYED);
        assertThat(response.remarks()).isEqualTo("Weather delay");
    }
}
