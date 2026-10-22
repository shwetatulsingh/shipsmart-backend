package com.shipsmart.shipment.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * A stable JSON shape for paginated results. Returning Spring's {@code Page}
 * directly would expose its internal structure, which can change between
 * Spring versions (Spring Data logs a warning when you do it).
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
