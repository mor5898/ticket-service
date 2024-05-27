package com.benevolo.service;

import com.benevolo.DTO.SearchDTO;
import com.benevolo.entity.Booking;
import com.benevolo.repo.BookingRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    public List<Booking> findByEventIdAndSearch(String eventId, int page, SearchDTO search) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        query.append("SELECT b FROM Booking AS b WHERE b.eventId = :eventId");
        parameters.put("eventId", eventId);

        String term = search.term();
        if(term != null && !term.isBlank()) {
            term = "%" + term + "%";
            query.append(" and (b.id LIKE :term OR b.customer.email LIKE :term)");
            parameters.put("term", term);
        }

        String bookedFrom = search.bookedFrom();
        if(bookedFrom != null && !bookedFrom.isBlank() && !bookedFrom.equals("undefined")) {
            query.append(" and b.bookedAt >= :bookedFrom");
            parameters.put("bookedFrom", LocalDate.parse(bookedFrom).atStartOfDay());
        }

        return bookingRepo.find(query.toString(), parameters).page(page, 15).list();
    }

}
