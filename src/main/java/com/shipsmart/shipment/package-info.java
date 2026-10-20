/**
 * Shipment module. Self-contained so it can later be extracted into its own
 * microservice: other modules must talk to it only through its service layer
 * or its Kafka events, never through its entities or repositories.
 */
package com.shipsmart.shipment;
