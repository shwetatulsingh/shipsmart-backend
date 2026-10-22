package com.shipsmart.shipment.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.shipsmart.shipment.enums.ShipmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * An immutable audit record of one status change (Flyway migration V2).
 *
 * <p>The relationship is one-directional (history → shipment). Loading a
 * shipment never drags in its full history; history is queried explicitly
 * through its repository when the history endpoint needs it.
 */
@Entity
@Table(name = "shipment_status_history")
@EntityListeners(AuditingEntityListener.class)
public class ShipmentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false, updatable = false)
    private Shipment shipment;

    /** {@code null} for the first entry, when the shipment is created. */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "previous_status", updatable = false, length = 30)
    private ShipmentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "new_status", nullable = false, updatable = false, length = 30)
    private ShipmentStatus newStatus;

    @CreatedDate
    @Column(name = "changed_at", nullable = false, updatable = false)
    private Instant changedAt;

    @Column(name = "remarks", updatable = false, length = 500)
    private String remarks;

    /** Required by JPA; not for application code. */
    protected ShipmentStatusHistory() {
    }

    public ShipmentStatusHistory(Shipment shipment,
                                 ShipmentStatus previousStatus,
                                 ShipmentStatus newStatus,
                                 String remarks) {
        this.shipment = shipment;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public ShipmentStatus getPreviousStatus() {
        return previousStatus;
    }

    public ShipmentStatus getNewStatus() {
        return newStatus;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public String getRemarks() {
        return remarks;
    }
}
