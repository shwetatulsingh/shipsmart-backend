package com.shipsmart.shipment.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * A postal address embedded into the owning table (no table of its own).
 * {@link Shipment} embeds it twice, prefixing the columns with
 * {@code pickup_} and {@code delivery_}.
 */
@Embeddable
public class Address {

    @Column(name = "line1")
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    /** ISO 3166-1 alpha-2, e.g. "IN". */
    @Column(name = "country_code")
    private String countryCode;

    /** Required by JPA; not for application code. */
    protected Address() {
    }

    public Address(String line1, String line2, String city, String state, String postalCode, String countryCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address other)) {
            return false;
        }
        return Objects.equals(line1, other.line1)
                && Objects.equals(line2, other.line2)
                && Objects.equals(city, other.city)
                && Objects.equals(state, other.state)
                && Objects.equals(postalCode, other.postalCode)
                && Objects.equals(countryCode, other.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line1, line2, city, state, postalCode, countryCode);
    }
}
