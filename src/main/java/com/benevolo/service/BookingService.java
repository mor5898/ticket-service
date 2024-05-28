package com.benevolo.service;

import com.benevolo.DTO.SearchDTO;
import com.benevolo.entity.Booking;
import com.benevolo.repo.BookingRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class BookingService {

    private final BookingRepo bookingRepo;

    @Inject
    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public List<Booking> findByEventIdAndSearch(String eventId, int page, MultivaluedMap<String, String> queryParams) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        query.append("SELECT b FROM Booking AS b WHERE b.eventId = :eventId");
        parameters.put("eventId", eventId);

        String term = extractFromMap(queryParams, "term");
        if(term != null && !term.isBlank()) {
            term = "%" + term + "%";
            query.append(" and (b.id LIKE :term OR b.customer.email LIKE :term)");
            parameters.put("term", term);
        }

        String bookedFrom = extractFromMap(queryParams, "dateFrom");
        if(bookedFrom != null && !bookedFrom.isBlank()) {
            query.append(" and b.bookedAt >= :bookedFrom");
            parameters.put("bookedFrom", LocalDate.parse(bookedFrom).atStartOfDay());
        }

        String bookedTo = extractFromMap(queryParams, "dateTo");
        if(bookedTo != null && !bookedTo.isBlank()) {
            query.append(" and b.bookedAt < :bookedTo");
            parameters.put("bookedTo", LocalDate.parse(bookedTo).plusDays(1).atStartOfDay());
        }

        String priceFrom = extractFromMap(queryParams, "priceFrom");
        if(priceFrom != null && !priceFrom.isBlank()) {
            query.append(" and b.totalPrice >= :priceFrom");
            parameters.put("priceFrom", Integer.valueOf(priceFrom));
        }

        String priceTo = extractFromMap(queryParams, "priceTo");
        if(priceTo != null && !priceTo.isBlank()) {
            query.append(" and b.totalPrice <= :priceTo");
            parameters.put("priceTo", Integer.valueOf(priceTo));
        }

        return bookingRepo.find(query.toString(), parameters).page(page, 15).list();
    }

    private String extractFromMap(MultivaluedMap<String, String> map, String key) {
        final List<String> value = map.get(key);
        if(value != null && !value.isEmpty()) {
            return value.get(0);
        }
        return null;
    }

}
