package com.shipsmart.shipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Address as sent and returned by the API. Size limits match the database columns. */
public record AddressDto(
        @NotBlank @Size(max = 200) String line1,
        @Size(max = 200) String line2,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String state,
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 -]{1,18}[A-Za-z0-9]$",
                 message = "must be 3-20 letters, digits, spaces or hyphens")
        String postalCode,
        @NotBlank
        @Pattern(regexp = "^[A-Z]{2}$", message = "must be a 2-letter ISO 3166 country code, e.g. IN")
        String countryCode) {
}
