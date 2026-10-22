-- Shipments. All timestamps are UTC (see README "Time convention").
-- Written in portable SQL so the same script runs on MySQL 8.4 and on the
-- H2 (MySQL mode) database used by the default test suite.

CREATE TABLE shipments (
    id                           BIGINT        NOT NULL AUTO_INCREMENT,
    tracking_number              VARCHAR(20)   NOT NULL,
    customer_name                VARCHAR(120)  NOT NULL,
    customer_email               VARCHAR(254)  NOT NULL,

    pickup_line1                 VARCHAR(200)  NOT NULL,
    pickup_line2                 VARCHAR(200)  NULL,
    pickup_city                  VARCHAR(100)  NOT NULL,
    pickup_state                 VARCHAR(100)  NOT NULL,
    pickup_postal_code           VARCHAR(20)   NOT NULL,
    pickup_country_code          VARCHAR(2)    NOT NULL,

    delivery_line1               VARCHAR(200)  NOT NULL,
    delivery_line2               VARCHAR(200)  NULL,
    delivery_city                VARCHAR(100)  NOT NULL,
    delivery_state               VARCHAR(100)  NOT NULL,
    delivery_postal_code         VARCHAR(20)   NOT NULL,
    delivery_country_code        VARCHAR(2)    NOT NULL,

    package_description          VARCHAR(500)  NOT NULL,
    package_weight_kg            DECIMAL(10,3) NOT NULL,
    status                       VARCHAR(30)   NOT NULL,
    estimated_delivery_date      DATE          NULL,

    created_at                   DATETIME(6)   NOT NULL,
    updated_at                   DATETIME(6)   NOT NULL,
    version                      BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_shipments PRIMARY KEY (id),
    CONSTRAINT uk_shipments_tracking_number UNIQUE (tracking_number),
    CONSTRAINT ck_shipments_package_weight_positive CHECK (package_weight_kg > 0)
);

-- "List shipments with status X, newest first"
CREATE INDEX idx_shipments_status_created_at ON shipments (status, created_at);

-- "List all shipments, newest first"
CREATE INDEX idx_shipments_created_at ON shipments (created_at);
