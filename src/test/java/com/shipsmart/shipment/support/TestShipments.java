package com.shipsmart.shipment.support;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.shipsmart.shipment.dto.AddressDto;
import com.shipsmart.shipment.dto.CreateShipmentRequest;
import com.shipsmart.shipment.entity.Address;
import com.shipsmart.shipment.entity.Shipment;

/** Realistic, fictional test data shared by the shipment tests. */
public final class TestShipments {

    private TestShipments() {
    }

    public static Address pickupAddress() {
        return new Address("Unit 4, Andheri Logistics Park", "Marol Industrial Area", "Mumbai", "Maharashtra", "400059", "IN");
    }

    public static Address deliveryAddress() {
        return new Address("12 MG Road", null, "Bengaluru", "Karnataka", "560001", "IN");
    }

    public static Shipment shipment(String trackingNumber) {
        return new Shipment(
                trackingNumber,
                "Priya Sharma",
                "priya.sharma@example.com",
                pickupAddress(),
                deliveryAddress(),
                "Laptop, boxed",
                new BigDecimal("2.450"),
                LocalDate.of(2030, 1, 15));
    }

    public static CreateShipmentRequest createRequest() {
        return new CreateShipmentRequest(
                "  Priya Sharma ",
                "Priya.Sharma@Example.com",
                new AddressDto("Unit 4, Andheri Logistics Park", "Marol Industrial Area", "Mumbai", "Maharashtra", "400059", "IN"),
                new AddressDto("12 MG Road", "  ", "Bengaluru", "Karnataka", "560001", "IN"),
                "Laptop, boxed",
                new BigDecimal("2.450"),
                LocalDate.of(2030, 1, 15));
    }
}
