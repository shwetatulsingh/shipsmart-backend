-- One row per status change. previous_status is NULL for the initial CREATED entry.

CREATE TABLE shipment_status_history (
    id                BIGINT        NOT NULL AUTO_INCREMENT,
    shipment_id       BIGINT        NOT NULL,
    previous_status   VARCHAR(30)   NULL,
    new_status        VARCHAR(30)   NOT NULL,
    changed_at        DATETIME(6)   NOT NULL,
    remarks           VARCHAR(500)  NULL,

    CONSTRAINT pk_shipment_status_history PRIMARY KEY (id)
);

-- "History for shipment N, in order". Created before the foreign key so MySQL
-- reuses it for the constraint instead of adding a second, redundant index.
CREATE INDEX idx_status_history_shipment_changed_at
    ON shipment_status_history (shipment_id, changed_at);

ALTER TABLE shipment_status_history
    ADD CONSTRAINT fk_status_history_shipment
    FOREIGN KEY (shipment_id) REFERENCES shipments (id);
