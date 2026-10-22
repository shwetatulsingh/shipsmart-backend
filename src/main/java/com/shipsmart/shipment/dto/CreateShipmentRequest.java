package com.shipsmart.shipment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Body of {@code POST /api/v1/shipments}. The tracking number, status and
 * timestamps are assigned by the server, so clients cannot send them.
 */
public record CreateShipmentRequest(
        @NotBlank @Size(max = 120) String customerName,
        @NotBlank @Email @Size(max = 254) String customerEmail,
        @NotNull @Valid AddressDto pickupAddress,
        @NotNull @Valid AddressDto deliveryAddress,
        @NotBlank @Size(max = 500) String packageDescription,
        @NotNull
        @DecimalMin(value = "0.001", message = "must be at least 0.001 kg")
        @DecimalMax(value = "1000.000", message = "must not exceed 1000 kg")
        @Digits(integer = 7, fraction = 3)
        BigDecimal packageWeightKg,
        @FutureOrPresent LocalDate estimatedDeliveryDate) {
}
