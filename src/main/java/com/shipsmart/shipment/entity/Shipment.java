package com.shipsmart.shipment.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.shipsmart.shipment.enums.ShipmentStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * A shipment, mapped to the {@code shipments} table created by Flyway
 * migration V1. Column definitions live in the migration; the annotations
 * here only describe the mapping, which Hibernate validates at startup.
 */
@Entity
@Table(name = "shipments")
@EntityListeners(AuditingEntityListener.class)
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false, updatable = false, length = 20)
    private String trackingNumber;

    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 254)
    private String customerEmail;

    @Embedded
    @AttributeOverride(name = "line1", column = @Column(name = "pickup_line1"))
    @AttributeOverride(name = "line2", column = @Column(name = "pickup_line2"))
    @AttributeOverride(name = "city", column = @Column(name = "pickup_city"))
    @AttributeOverride(name = "state", column = @Column(name = "pickup_state"))
    @AttributeOverride(name = "postalCode", column = @Column(name = "pickup_postal_code"))
    @AttributeOverride(name = "countryCode", column = @Column(name = "pickup_country_code"))
    private Address pickupAddress;

    @Embedded
    @AttributeOverride(name = "line1", column = @Column(name = "delivery_line1"))
    @AttributeOverride(name = "line2", column = @Column(name = "delivery_line2"))
    @AttributeOverride(name = "city", column = @Column(name = "delivery_city"))
    @AttributeOverride(name = "state", column = @Column(name = "delivery_state"))
    @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code"))
    @AttributeOverride(name = "countryCode", column = @Column(name = "delivery_country_code"))
    private Address deliveryAddress;

    @Column(name = "package_description", nullable = false, length = 500)
    private String packageDescription;

    @Column(name = "package_weight_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal packageWeightKg;

    // Stored as VARCHAR. Without @JdbcTypeCode, Hibernate 6 expects a native
    // MySQL ENUM column and schema validation fails against our VARCHAR(30).
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "status", nullable = false, length = 30)
    private ShipmentStatus status;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Optimistic locking: two concurrent updates of the same shipment can't
    // silently overwrite each other; the second fails instead.
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /** Required by JPA; not for application code. */
    protected Shipment() {
    }

    /** Creates a new shipment in status {@link ShipmentStatus#CREATED}. */
    public Shipment(String trackingNumber,
                    String customerName,
                    String customerEmail,
                    Address pickupAddress,
                    Address deliveryAddress,
                    String packageDescription,
                    BigDecimal packageWeightKg,
                    LocalDate estimatedDeliveryDate) {
        this.trackingNumber = trackingNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.packageDescription = packageDescription;
        this.packageWeightKg = packageWeightKg;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.status = ShipmentStatus.CREATED;
    }

    /**
     * Sets the new status. Deliberately has no rules yet: Phase 3 validates
     * transitions in the service layer and records history in the same
     * transaction.
     */
    public void changeStatus(ShipmentStatus newStatus) {
        this.status = newStatus;
    }

    public Long getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public BigDecimal getPackageWeightKg() {
        return packageWeightKg;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }
}
