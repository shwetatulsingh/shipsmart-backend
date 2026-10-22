package com.shipsmart.shipment.mapper;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.shipsmart.shipment.dto.AddressDto;
import com.shipsmart.shipment.dto.CreateShipmentRequest;
import com.shipsmart.shipment.dto.ShipmentResponse;
import com.shipsmart.shipment.dto.StatusHistoryResponse;
import com.shipsmart.shipment.entity.Address;
import com.shipsmart.shipment.entity.Shipment;
import com.shipsmart.shipment.entity.ShipmentStatusHistory;

/**
 * Converts between API DTOs and JPA entities.
 *
 * <p>Hand-written on purpose: with a few small types, plain Java is easier to
 * read and debug than generated code. MapStruct becomes worth it when there
 * are many large mappings.
 */
@Component
public class ShipmentMapper {

    /**
     * @param trackingNumber assigned by the service; never taken from the client
     */
    public Shipment toNewEntity(CreateShipmentRequest request, String trackingNumber) {
        return new Shipment(
                trackingNumber,
                request.customerName().strip(),
                request.customerEmail().strip().toLowerCase(Locale.ROOT),
                toAddress(request.pickupAddress()),
                toAddress(request.deliveryAddress()),
                request.packageDescription().strip(),
                request.packageWeightKg(),
                request.estimatedDeliveryDate());
    }

    public ShipmentResponse toResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getCustomerName(),
                shipment.getCustomerEmail(),
                toDto(shipment.getPickupAddress()),
                toDto(shipment.getDeliveryAddress()),
                shipment.getPackageDescription(),
                shipment.getPackageWeightKg(),
                shipment.getStatus(),
                shipment.getEstimatedDeliveryDate(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt());
    }

    public StatusHistoryResponse toResponse(ShipmentStatusHistory history) {
        return new StatusHistoryResponse(
                history.getId(),
                history.getPreviousStatus(),
                history.getNewStatus(),
                history.getChangedAt(),
                history.getRemarks());
    }

    Address toAddress(AddressDto dto) {
        return new Address(
                dto.line1().strip(),
                dto.line2() == null || dto.line2().isBlank() ? null : dto.line2().strip(),
                dto.city().strip(),
                dto.state().strip(),
                dto.postalCode().strip(),
                dto.countryCode());
    }

    AddressDto toDto(Address address) {
        return new AddressDto(
                address.getLine1(),
                address.getLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountryCode());
    }
}
